package project.ForumWebApp.constants;

public class ValidationConstants {
    // Post constants
    public static final int TITLE_MIN_LENGTH = 16;
    public static final int TITLE_MAX_LENGTH = 64;
    public static final String TITLE_LENGTH_MESSAGE = "Title must be between " + TITLE_MIN_LENGTH + " and " + TITLE_MAX_LENGTH + " characters.";

    public static final int DESCRIPTION_MIN_LENGTH = 32;
    public static final int DESCRIPTION_MAX_LENGTH = 8192;
    public static final String DESCRIPTION_LENGTH_MESSAGE = "Description must be between " + DESCRIPTION_MIN_LENGTH + " and " + DESCRIPTION_MAX_LENGTH + " characters.";

    public static final String NOT_BLANK_MESSAGE = "Field must not be blank.";

    // Constants for ApplicationUser
    public static final int FIRST_NAME_MAX_LEN = 32;
    public static final int FIRST_NAME_MIN_LEN = 4;
    public static final String FIRST_NAME_LENGTH_MESSAGE = "First name must be between " + FIRST_NAME_MIN_LEN + " and " + FIRST_NAME_MAX_LEN + " characters.";

    public static final int LAST_NAME_MAX_LEN = 32;
    public static final int LAST_NAME_MIN_LEN = 4;
    public static final String LAST_NAME_LENGTH_MESSAGE = "Last name must be between " + LAST_NAME_MIN_LEN + " and " + LAST_NAME_MAX_LEN + " characters.";

    public static final int EMAIL_MAX_LEN = 255;
    public static final String EMAIL_LENGTH_MESSAGE = "Email should not be more than " + EMAIL_MAX_LEN + " characters.";
    public static final String EMAIL_NOT_BLANK_MESSAGE = "Email is mandatory";
    public static final String EMAIL_VALID_MESSAGE = "Email should be valid";

    public static final String PASSWORD_NOT_BLANK_MESSAGE = "Password is mandatory";
    public static final String PASSWORD_LENGTH_MESSAGE = "Password must be at least 6 characters long";
    public static final int PASSWORD_MIN_LEN = 6;

    public static final int USERNAME_MAX_LEN = 20;
    public static final int USERNAME_MIN_LEN = 4;     public static final String USERNAME_LENGTH_MESSAGE = "Username must be between " + USERNAME_MIN_LEN + " and " + USERNAME_MAX_LEN + " characters.";
    public static final String USERNAME_NOT_BLANK_MESSAGE = "Username is mandatory";

    public static final int PHOTO_URL_MAX_LEN = 255;
    public static final String PHOTO_URL_LENGTH_MESSAGE = "Photo URL should not be more than " + PHOTO_URL_MAX_LEN + " characters.";
    public static final String PHOTO_URL_NOT_BLANK_MESSAGE = "Photo URL is mandatory";

    public static final String DEFAULT_PHOTO_URL = "https://plus.unsplash.com/premium_photo-1677094310899-02303289cadf?q=80&w=1932&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D";

    // Constants for Tags
    public static final int TAG_MIN_LEN = 1;
    public static final int TAG_MAX_LEN = 32;
    public static final String TAG_LENGTH_MESSAGE = "Tag must be between " + TAG_MIN_LEN + " and " + TAG_MAX_LEN + " characters.";

    // Constants for Comments
    public static final int COMMENT_MIN_LEN = 1;
    public static final int COMMENT_MAX_LEN = 1024;
    public static final String COMMENT_LENGTH_MESSAGE = "Comment must be between " + COMMENT_MIN_LEN + " and " + COMMENT_MAX_LEN + " characters.";
}
