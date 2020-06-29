//Диалоговое окно с подтверждением удаления товара из списка

package com.example.myapplication_makeup.dialogs;

import android.os.Bundle;

import android.support.v4.app.DialogFragment;

import android.app.Dialog;
import android.app.AlertDialog;

import android.content.DialogInterface;

import com.example.myapplication_makeup.MainList;

public class ListDialogDelete extends DialogFragment{
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		builder
			.setTitle("ВНИМАНИЕ!")
			.setMessage("Вы подтверждаете удаление товара из списка?")
			.setCancelable(true)
			.setPositiveButton(
					"Нет",
					new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int id){
							dialog.cancel();
						}
					}
				)
			.setNegativeButton(
					"Да",
					new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int id){
							((MainList)getActivity()).deleteProduct();
						}
					}
				);
		
		return builder.create();
	}
}