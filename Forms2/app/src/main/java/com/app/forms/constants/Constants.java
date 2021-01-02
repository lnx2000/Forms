package com.app.forms.constants;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Constants {

    public static final String[] textTypes = {"Text", "Number", "Long", "Masked"};
    public static final int typeTextField = 0;
    public static final int typeSingleCheck = 1;
    public static final int typeMultipleCheck = 2;
    public static final int typepoll = 3;
    public static final int typerating = 4;
    public static final int typeupload = 5;
    public static final int formAdded = 1;
    public static final int typeSubmit = 25;
    public static final int typeTextMsg = 100;
    public static final int formUIDLength = 8;
    public static final String changesSaved = "Changes Saved";
    public static final String changesNotSaved = "Error while saving";
    public static final int editFragment = 0;
    public static final int settingFragment = 1;
    public static final int returnFormCreateFormActivity = 100;
    public static final SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a", Locale.getDefault());
    public static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    public static final SimpleDateFormat parseDateTime = new SimpleDateFormat("dd MMM yyyyhh:mm a", Locale.getDefault());
    public static final String githubUrl = "https://github.com/lnx2000";
    public static final String linkedinUrl = "https://www.linkedin.com/in/omkar-amilkanthwar-994809186/";
    public static final String emailID = "omkar.amilkanthwar210@gmail.com";
    public static final SimpleDateFormat dFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    public static final String formBaseUrl = "http://com.app.forms/f/";
    public static final String notAccepting = " is no longer accepting responses.\nTry contacting the owner of the form if you think this a mistake";
    public static final int responseSubmitted = 1;
    public static final int reportSubmitted = 2;
    public static final int alreadyfilled = 3;

}
