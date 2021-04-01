package com.StartupBBSR.competo.Utils;

import android.view.View;

import com.StartupBBSR.competo.databinding.OnboardingLayoutItemBinding;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

public class ViewPagerTransformer implements ViewPager2.PageTransformer {


    @Override
    public void transformPage(@NonNull View page, float position) {
        OnboardingLayoutItemBinding binding = OnboardingLayoutItemBinding.bind(page);
        int pageWidth = page.getWidth();

        if (position < -1) {
            page.setAlpha(1);
        } else if (position <= 1) {
//            binding.tvOnboardingTitle.setTranslationX((float) (-(1 - position) * 0.5 * pageWidth));
//            binding.tvOnboardingDescription.setTranslationX((float) (-(1 - position) * 1.5 * pageWidth));
            binding.tvOnboardingTitle.setTranslationX((position) * (pageWidth / 4));
            binding.tvOnboardingDescription.setTranslationX((position) * (pageWidth / 2));
            binding.ivOnboardingImage.setTranslationX((position)*(pageWidth/1));
        }else {
            page.setAlpha(1);
        }
    }
}

