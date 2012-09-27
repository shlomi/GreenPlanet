package com.example.greenplanet;

import android.location.Location;

/**  code comments  */

// ����� ���� ����� �� �� ������� ������� ����� �� ������� ���� ������� �������� ��������
public class StaticContent 
{
	// ��� ��� - ������� ������� ������ ���.��.�� ������ ���������� ���� ����� "���"
	// ��� ��� - ������� ��� ������� ���.��.�� ���� ����� ������������ ���� ����� "����"
	static boolean isGPSlocationState = false; 
	
	// ��� ��� - ������� ���� ����� ������ ������� ������ ������ ����� ���� �������
	// ��� ��� - ������� ���� ����� ������ ���� �� �������� ����� ������ �����
	static boolean isGPSLocationType = false;
	
	// ����� ������ ������
	static String Path = null; 

	// ������� ����� ������ ����� ���� ������� ���.��.��
	static Location location = null;
	
	// ������� ����� ������ ���� ������ ������� �����   
	static String city = null,   
			      street = null,
			      number = null;
		
	// ��� ��� - ������� ������ ���� �� ����������� ����
	//  ������� �� ����� �� ����� �� ����� ��� ��� - ���� ����� ���� ����
	static boolean sendBtnState = false;
	
	// ��� ��� - ������� �������� ���.��.�� && ������ �� ������ �������� ������ 
	static boolean locationServiceEndedSuccessfully = false;
	
	// ��� ��� - ������� ������� ������ �� ����� ���� ������ 
	// ���� �� ��� ���� ����� ��� ��������� ��� ������
	static boolean uploadServiceEndedSuccessfully = false;	
	
	// ���� �� ����� ������
	static String ImageCaptureTime = null;
	
	// ������ �� ����� ������  
	static String ImageCaptureDate = null;
	
	//
	static String serverResponseMessage = "Error";
	
	//
	static int serverResponseCode = 0;
	
	//
	static String serverResponseBody = null;
	
	/**          */
	static enum MY_NOTIFICATION_ID
	{
		GREENPLANET_ACTIVITY ,
		SEND_ACTIVITY ,
		ADRRESS_ACTIVITY ,
		LOCATION_SERVICE ,
		UPLOAD_SERVICE ;
		// ADAPTER ;
	}
}
