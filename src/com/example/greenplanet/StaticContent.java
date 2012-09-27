package com.example.greenplanet;

import android.location.Location;

/**  code comments  */

// למעשה רצוי להפוך את כל המשתנים לפרטיים ולבצע את הפעולות מולם באמצעות מאפיינים ציבוריים
public class StaticContent 
{
	// ערך אמת - משמעותו השתמשנו בשירות הגי.פי.אס וכפתור סנדאקטיבטי צריך להיות "שלח"
	// ערך שקר - משמעותו שלא השתמשנו בגי.פי.אס ולכן כפתור הסנדאקטיביטי צריך להיות "המשך"
	static boolean isGPSlocationState = false; 
	
	// ערך אמת - משמעותו שסוג נתוני המיקום שנמצאים בידינו צריכים להיות מסוג לוקיישן
	// ערך שקר - משמעותו שסוג נתוני המיקום שלנו הם המחרוזות מיקום שנלקחו ידנית
	static boolean isGPSLocationType = false;
	
	// הנתיב לתמונה ששמרנו
	static String Path = null; 

	// אובייקט נתוני המיקום כשהוא נלקח באמצעות הגי.פי.אס
	static Location location = null;
	
	// מחרוזות נתוני המיקום כשהם נלקחים מהמשתמש ידנית   
	static String city = null,   
			      street = null,
			      number = null;
		
	// ערך אמת - משמעותו שכפתור השלח של סנדאקטיביטי נלחץ
	//  רלוונטי רק במקרה בו הטקסט של הלחצן הוא שלח - למעט במקרה שהוא המשך
	static boolean sendBtnState = false;
	
	// ערך אמת - משמעותו שהשתמשנו בגי.פי.אס && קיבלנו את המיקום באמצעותו בהצלחה 
	static boolean locationServiceEndedSuccessfully = false;
	
	// ערך אמת - משמעותו שהצלחנו להעלות את המידע לשרת בהצלחה 
	// לשרת יש קוד חוזר שנותן לנו אינדיקציה האם הצלחנו
	static boolean uploadServiceEndedSuccessfully = false;	
	
	// השעה בה נדגמה התמונה
	static String ImageCaptureTime = null;
	
	// התאריך בו נדגמה התמונה  
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
