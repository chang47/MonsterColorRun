package com.brnleehng.worldrunner;


import java.util.ArrayList;
import java.util.List;

import DB.Model.RunningLog;
import Singleton.RunningLogList;
import android.app.Fragment;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LogFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final SoundPool sp = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
		final int soundIds[] = new int[2];
		soundIds[0] = sp.load(getActivity().getBaseContext(), R.raw.click, 1);
		soundIds[1] = sp.load(getActivity().getBaseContext(), R.raw.enterbattle, 1);
		View view = inflater.inflate(R.layout.logfragment_activity, container, false);
		List<RunningLog> list = new ArrayList<RunningLog>();
		// in case something happened with the list with loading
		if (RunningLogList.getList() != null) {
			list = RunningLogList.getList();
		}
		
		// TODO need to make simple array adapter.

		return view;
	}
}
