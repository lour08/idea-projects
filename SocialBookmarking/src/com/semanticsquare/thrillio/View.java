package com.semanticsquare.thrillio;

import com.semanticsquare.thrillio.constants.KidFriendlyStatus;
import com.semanticsquare.thrillio.constants.UserType;
import com.semanticsquare.thrillio.controllers.BookmarkController;
import com.semanticsquare.thrillio.entities.Bookmark;
import com.semanticsquare.thrillio.entities.User;
import com.semanticsquare.thrillio.partner.Shareable;

public class View {
    public static void brose(User user, Bookmark[][] bookmarks){
        System.out.println(user.getEmail() + " is browsing items...");
        int bookmarkCount = 0;

        for (Bookmark[] bookmarkList :
                bookmarks) {
            for (Bookmark bookmark :
                    bookmarkList) {
                if (bookmarkCount < DataStore.USER_BOOKMARK_LIMIT){
                    boolean isBookmarked = getBookmarkDecision(bookmark);
                    if (isBookmarked){
                        bookmarkCount++;
                        BookmarkController.getInstance().saveUserBookmark(user, bookmark);

                        System.out.println(bookmark);

                    }
                }
                if (user.getUserType().equals(UserType.EDITOR) || user.getUserType().equals(UserType.CHIEF_EDITOR)){
                    if (bookmark.isKidFriendlyElegible() && bookmark.getKidFriendlyStatus().equals(KidFriendlyStatus.UNKNOWN)){
                       String kidFriendlyStatus = getKidFriendlyStatusDecision();
                       if (!kidFriendlyStatus.equals(KidFriendlyStatus.UNKNOWN)){
                           BookmarkController.getInstance().setKidFriendlyStatus(user, kidFriendlyStatus, bookmark);
                       }
                    }

                    //Sharing
                    if (bookmark.getKidFriendlyStatus().equals(KidFriendlyStatus.APPROVED)
                            && bookmark instanceof Shareable){
                        boolean isShared = getShareDecision();
                        if (isShared){
                            BookmarkController.getInstance().share(user, bookmark);
                        }
                    }
                }
            }
        }
        for (int i = 0; i < DataStore.USER_BOOKMARK_LIMIT; i++) {
            int typeOffset = (int) (Math.random() * DataStore.BOOKMARK_TYPES_COUNT);
            int bookmarkOffset = (int)(Math.random() * DataStore.BOOKMARK_COUNT_PER_TYPE);

            Bookmark bookmark = bookmarks[typeOffset][bookmarkOffset];

            BookmarkController.getInstance().saveUserBookmark(user, bookmark);

            System.out.println(bookmark);

        }
    }

    private static boolean getShareDecision() {
        return Math.random() < 0.5;
    }

    private static String getKidFriendlyStatusDecision() {
        return Math.random() < 0.4  ? KidFriendlyStatus.APPROVED :
                (Math.random() >= 0.5 && Math.random() < 0.8) ? KidFriendlyStatus.REJECTED : KidFriendlyStatus.UNKNOWN;
    }


    private static boolean getBookmarkDecision(Bookmark bookmark) {
        return Math.random() < 0.5;
    }
    /*public static void bookmark(User user, Bookmark[][] bookmarks){
        System.out.println(user.getEmail() + " is bookmarking");
        for (int i = 0; i < DataStore.USER_BOOKMARK_LIMIT; i++) {
            int typeOffset = (int) (Math.random() * DataStore.BOOKMARK_TYPES_COUNT);
            int bookmarkOffset = (int)(Math.random() * DataStore.BOOKMARK_COUNT_PER_TYPE);

            Bookmark bookmark = bookmarks[typeOffset][bookmarkOffset];

            BookmarkController.getInstance().saveUserBookmark(user, bookmark);

            System.out.println(bookmark);

        }
    }*/
}
