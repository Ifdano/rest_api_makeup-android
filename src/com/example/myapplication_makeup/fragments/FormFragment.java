//Фрагмент from_fragment

package com.example.myapplication_makeup.fragments;

import android.os.Bundle;

import android.support.v4.app.Fragment;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import com.example.myapplication_makeup.R;

public class FormFragment extends Fragment{
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.form_fragment, container, false);
		return view;
	}
}