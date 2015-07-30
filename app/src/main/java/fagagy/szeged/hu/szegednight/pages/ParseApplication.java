package fagagy.szeged.hu.szegednight.pages;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by TheSorrow on 15/07/30.
 */
public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate(); Parse.enableLocalDatastore(this);
        Parse.initialize(this, "JQGvIPiUsllFbVsmq63xWd34UQxrKOusu2M5XLlr", "x24qzq57nI7xKwkl89M6zbuIez35ILsywXasVKee");
    }
}

