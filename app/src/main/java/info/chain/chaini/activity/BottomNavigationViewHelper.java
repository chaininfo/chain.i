/*
 * Copyright (C) 2011 Patrik Åkerfeldt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.chain.chaini.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;

import java.lang.reflect.Field;

/**
 * An interface which defines the contract between a ViewFlow and a
 * FlowIndicator.<br/>
 * A FlowIndicator is responsible to show an visual indicator on the total views
 * number and the current visible view.<br/>
 * 
 */
public class BottomNavigationViewHelper {

	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	@SuppressLint("RestrictedApi")
	public static void disableShiftMode(BottomNavigationView navigationView) {

		BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigationView.getChildAt(0);
		try {
			Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
			shiftingMode.setAccessible(true);
			shiftingMode.setBoolean(menuView, false);
			shiftingMode.setAccessible(false);

			for (int i = 0; i < menuView.getChildCount(); i++) {
				BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(i);
				itemView.setShiftingMode(false);
				itemView.setChecked(itemView.getItemData().isChecked());
			}

		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
