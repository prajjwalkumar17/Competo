package com.StartupBBSR.competo.Fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.StartupBBSR.competo.Adapters.TagRecyclerAdapter;
import com.StartupBBSR.competo.Listeners.addOnTextChangeListener;
import com.StartupBBSR.competo.Models.EventModel;
import com.StartupBBSR.competo.R;
import com.StartupBBSR.competo.Utils.Constant;
import com.StartupBBSR.competo.databinding.FragmentAddEventBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;


public class AddEventFragment extends Fragment {

    private FragmentAddEventBinding binding;

    private static final int REQUEST_PHOTO_CODE = 123;

    private TagRecyclerAdapter adapter;
    private List<String> tagDataSet;
    private ChipGroup tagChipGroup;

    private Calendar calendar;

    private String imageString;
    private Uri eventImageUri, eventImageDownloadUri;
    private UploadTask uploadTask;

    private NavController navController;

    private int flag = 0, emptyFlag = 0, statusFlag = -1, imageFlag = 0;

    private Constant constant;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestoreDB;
    private FirebaseStorage firebaseStorage;
    private String organizerID;

    private Long eventDateStamp, eventTimeStamp;

    private EventModel eventModel;
    private String liveEventid, draftEventid, eventid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddEventBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        firebaseAuth = FirebaseAuth.getInstance();
        firestoreDB = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        organizerID = firebaseAuth.getUid();

        constant = new Constant();
        calendar = Calendar.getInstance();


        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                updateDateLabel();
            }
        };

        TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                calendar.set(Calendar.HOUR, hour);
                calendar.set(Calendar.MINUTE, minute);
                updateTimeLabel();
            }
        };

        initTagDataSet();

        tagChipGroup = binding.tagsChipGroup;

        binding.topAppBar.setNavigationOnClickListener(view1 -> {
            navController.navigate(R.id.action_addEventFragment_to_manageEventMainFragment);
        });


        binding.ivPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });

        binding.DateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), date,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                        .show();

                binding.DateET.clearFocus();
            }
        });

        binding.TimeET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(getContext(), time, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), false)
                        .show();
            }
        });

        binding.recyclerViewTags.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewTags.setHasFixedSize(true);

        binding.ivPoster.setClipToOutline(true);

        adapter = new TagRecyclerAdapter(tagDataSet);
        adapter.setOnTagClickListener(new TagRecyclerAdapter.OnTagClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Chip chip = new Chip(getContext());

                TextView text = itemView.findViewById(R.id.tvTag);
                chip.setText(text.getText().toString());
                chip.setCloseIconVisible(true);
                chip.setCheckable(false);
                chip.setClickable(false);
                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tagChipGroup.removeView(view);
                    }
                });
                tagChipGroup.addView(chip);
                tagChipGroup.setVisibility(View.VISIBLE);
            }
        });
        binding.recyclerViewTags.setAdapter(adapter);


        binding.TagET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.recyclerViewTags.setVisibility(View.VISIBLE);
                String input = charSequence.toString();

                List<String> newChips = new ArrayList<>();

                for (String tag : tagDataSet) {
                    if (tag.contains(input)) {
                        newChips.add(tag);
                    }
                }

                adapter = new TagRecyclerAdapter(newChips);
                adapter.setOnTagClickListener(new TagRecyclerAdapter.OnTagClickListener() {
                    @Override
                    public void onItemClick(View itemView, int position) {
                        Chip chip = new Chip(getContext());

                        TextView text = itemView.findViewById(R.id.tvTag);
                        chip.setText(text.getText().toString());
                        chip.setCloseIconVisible(true);
                        chip.setCheckable(false);
                        chip.setClickable(false);
                        chip.setOnCloseIconClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                tagChipGroup.removeView(view);
                            }
                        });
                        tagChipGroup.addView(chip);
                        tagChipGroup.setVisibility(View.VISIBLE);

                        binding.recyclerViewTags.setVisibility(View.INVISIBLE);
                    }
                });
                binding.recyclerViewTags.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }

        });


        textChangedListener(binding.TitleET, binding.TitleTIL);
        textChangedListener(binding.DescriptionET, binding.DescriptionTIL);
        textChangedListener(binding.VenueET, binding.VenueTIL);
        textChangedListener(binding.DateET, binding.DateTIL);
        textChangedListener(binding.TimeET, binding.TimeTIL);
        textChangedListener(binding.linkET, binding.linkTIL);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        binding.switchStatus.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                binding.tvStatus.setText("Upload as live");
                binding.btnUploadEvent.setText("Upload Event");
                statusFlag = 1;
            } else {
                binding.tvStatus.setText("Save as draft");
                binding.btnUploadEvent.setText("Save as draft");
                statusFlag = 0;
            }
        });


        if ((EventModel) getArguments().getSerializable("editEvent") != null) {
//            Edit Event
            eventModel = (EventModel) getArguments().getSerializable("editEvent");
            flag = 1;
            binding.btnUploadEvent.setText("Save Changes");
            binding.topAppBar.setTitle("Edit Event");
        }


        if (flag != 0) {
//            User clicked edit event
//            loadData into views

            SimpleDateFormat simpleEventDateFormat = new SimpleDateFormat("dd-MMM-yy", Locale.US);
            SimpleDateFormat simpleEventTimeFormat = new SimpleDateFormat("KK:mm a", Locale.US);

            if (eventModel.getEventDateStamp() != null)
                binding.DateET.setText(simpleEventDateFormat.format(new Date(Long.parseLong(eventModel.getEventDateStamp().toString()))));

            if (eventModel.getEventTimeStamp() != null)
                binding.TimeET.setText(simpleEventTimeFormat.format(new Date(Long.parseLong(eventModel.getEventTimeStamp().toString()))));

            binding.TitleET.setText(eventModel.getEventTitle());
            binding.DescriptionET.setText(eventModel.getEventDescription());
            binding.VenueET.setText(eventModel.getEventVenue());
            binding.linkET.setText(eventModel.getEventLink());

            liveEventid = eventModel.getEventID();
            draftEventid = eventModel.getEventID();

            List<String> tags = eventModel.getEventTags();
            for (int i = 0; i < tags.size(); i++) {
                Chip chip = new Chip(getContext());
                chip.setText(tags.get(i));
                chip.setCloseIconVisible(true);
                chip.setCheckable(false);
                chip.setClickable(false);
                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tagChipGroup.removeView(view);
                    }
                });
                tagChipGroup.addView(chip);
                tagChipGroup.setVisibility(View.VISIBLE);
            }

            imageString = eventModel.getEventPoster();
            loadUsingGlide(imageString);

        }

        binding.btnUploadEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emptyFlag = 0;
                checkEmptyField(binding.TitleET, binding.TitleTIL);
                checkEmptyField(binding.DescriptionET, binding.DescriptionTIL);
                checkEmptyField(binding.VenueET, binding.VenueTIL);
                checkEmptyField(binding.DateET, binding.DateTIL);
                checkEmptyField(binding.TimeET, binding.TimeTIL);
                checkEmptyField(binding.linkET, binding.linkTIL);

                if (emptyFlag == 6) {
                    if (eventImageUri == null && imageString != null) {
                        imageFlag = 1;
                        uploadEvent();
                    } else {
                        imageFlag = 0;
                        updateInfo();
                    }
                }

            }
        });
    }

    private void updateInfo() {

        binding.btnUploadEvent.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);

        StorageReference storageReference = firebaseStorage.getReference()
                .child(constant.getEventPosters() + "/" + firestoreDB.collection(constant.getEvents()).get());

        if (eventImageUri != null) {
//            Compressing The Image
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), eventImageUri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

//                Higher the number, higher the quality
                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
                byte[] data = baos.toByteArray();

                uploadTask = storageReference.putBytes(data);

            } catch (IOException e) {
                e.printStackTrace();
            }

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(), "Photo Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Error Uploading Image:\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    binding.btnUploadEvent.setVisibility(View.VISIBLE);
                    binding.progressBar.setVisibility(View.GONE);
                }
            });

            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful())
                        throw task.getException();

                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        eventImageDownloadUri = task.getResult();
                        uploadEvent();

                    }
                }
            });

        } else {
            uploadEvent();
        }
    }


    private void uploadEvent() {

        binding.btnUploadEvent.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);

        String image;
        String title = binding.TitleET.getText().toString();
        String description = binding.DescriptionET.getText().toString();
        String venue = binding.VenueET.getText().toString();
        String link = binding.linkET.getText().toString();
        List<String> eventTags = new ArrayList<>();

        Long dateStamp, timeStamp;

        if (eventDateStamp != null && eventTimeStamp != null) {
            dateStamp = eventDateStamp;
            timeStamp = eventTimeStamp;
        } else {
            String date = binding.DateET.getText().toString();
            String time = binding.TimeET.getText().toString();

            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy", Locale.US);
            DateFormat timeFormat = new SimpleDateFormat("KK:mm a", Locale.US);
            Date mDate = null, mTime = null;
            try {
                mDate = (Date)dateFormat.parse(date);
                mTime = (Date)timeFormat.parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            dateStamp = mDate.getTime();
            timeStamp = mTime.getTime();
        }


        for (int i = 0; i < binding.tagsChipGroup.getChildCount(); ++i) {
            Chip chip = (Chip) binding.tagsChipGroup.getChildAt(i);
            eventTags.add(chip.getText().toString());
        }

        if (imageFlag == 0) {
            if (eventImageDownloadUri != null) {
                image = eventImageDownloadUri.toString();
            } else {
                image = null;
            }
        } else {
            image = imageString;
        }


        CollectionReference eventLivecollectionReference = firestoreDB.collection(constant.getEvents());
        CollectionReference eventDraftCollectionReference = firestoreDB.collection(constant.getDraftEvents());

        if (flag == 0) {
//        New Event. New id
//        Generate random id to use set else if id not needed we could directly use add()
            liveEventid = eventLivecollectionReference.document().getId();
            draftEventid = eventDraftCollectionReference.document().getId();
        }

        if (statusFlag == 0) {
//            Save as draft

            EventModel eventModel = new EventModel(image, title, description, venue, dateStamp, timeStamp, link, eventTags, organizerID, draftEventid);

            eventDraftCollectionReference.document(draftEventid).set(eventModel)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getContext(), "Event Uploaded", Toast.LENGTH_SHORT).show();
                            binding.btnUploadEvent.setVisibility(View.VISIBLE);
                            binding.progressBar.setVisibility(View.GONE);
                            navController.navigate(R.id.action_addEventFragment_to_manageEventMainFragment);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Upload failed:\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    binding.btnUploadEvent.setVisibility(View.VISIBLE);
                    binding.progressBar.setVisibility(View.GONE);
                }
            });

//        If saving as draft, if document exists in live, delete it
            eventLivecollectionReference.document(draftEventid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        eventLivecollectionReference.document(draftEventid).delete();
                    }
                }
            });

        } else {
//            Save as live
//            EventModel eventModel = new EventModel(image, title, description, venue, date, time, link, eventTags, organizerID, liveEventid);

            EventModel eventModel = new EventModel(image, title, description, venue, dateStamp, timeStamp, link, eventTags, organizerID, liveEventid);

            eventLivecollectionReference.document(liveEventid).set(eventModel)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getContext(), "Event Uploaded", Toast.LENGTH_SHORT).show();
                            binding.btnUploadEvent.setVisibility(View.VISIBLE);
                            binding.progressBar.setVisibility(View.GONE);
                            navController.navigate(R.id.action_addEventFragment_to_manageEventMainFragment);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Upload failed:\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    binding.btnUploadEvent.setVisibility(View.VISIBLE);
                    binding.progressBar.setVisibility(View.GONE);
                }
            });


            //        If saving as live, if document exists in draft, delete it
            eventDraftCollectionReference.document(liveEventid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        eventDraftCollectionReference.document(liveEventid).delete();
                    }
                }
            });


        }

    }

    private void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_PHOTO_CODE);

    }

    private void initTagDataSet() {
        tagDataSet = Arrays.asList(getResources().getStringArray(R.array.FilterChips));
    }

    private void updateDateLabel() {
        String myDateFormat = "dd-MMM-yy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myDateFormat, Locale.US);
        eventDateStamp = calendar.getTimeInMillis();
        binding.DateET.setText(simpleDateFormat.format(calendar.getTime()));
    }

    private void updateTimeLabel() {
        String myTimeFormat = "hh:mm a";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myTimeFormat, Locale.US);
        eventTimeStamp = calendar.getTimeInMillis();
        binding.TimeET.setText(simpleDateFormat.format(calendar.getTime()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PHOTO_CODE && resultCode == getActivity().RESULT_OK) {
            if (data == null) {
                Toast.makeText(getContext(), "Error Fetching Image", Toast.LENGTH_SHORT).show();
                return;
            }

            eventImageUri = data.getData();

            loadUsingGlide(eventImageUri.toString());
        }
    }


    private void loadUsingGlide(String imgurl) {
        Glide.with(this).
                load(imgurl).
                listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        binding.ivPosterProgressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        binding.ivPosterProgressBar.setVisibility(View.GONE);
                        return false;
                    }
                }).into(binding.ivPoster);
    }

    private void textChangedListener(TextInputEditText ET, TextInputLayout TIL) {
        ET.addTextChangedListener(new addOnTextChangeListener(getContext(), ET, TIL));
    }


    private void checkEmptyField(EditText et, TextInputLayout til) {
        if (et.getText().toString().isEmpty()) {
            til.setError("Field cannot be blank");
            til.setErrorEnabled(true);
            emptyFlag--;
        } else {
            emptyFlag++;
        }

    }
}