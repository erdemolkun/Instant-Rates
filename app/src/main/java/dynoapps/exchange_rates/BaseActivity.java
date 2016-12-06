package dynoapps.exchange_rates;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.ButterKnife;
import dynoapps.exchange_rates.util.L;

/**
 * Created by erdemmac on 06/12/2016.
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
    }


    private Toolbar mActionBarToolbar;

    protected Toolbar getActionBarToolbar() {
        if (mActionBarToolbar == null) {
            mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
            if (mActionBarToolbar != null) {
                setSupportActionBar(mActionBarToolbar);
            }
        }
        return mActionBarToolbar;
    }

    @LayoutRes
    public abstract int getLayoutId();

    protected void setNavigationIcon(@DrawableRes Integer resId) {
        Drawable drawable = ContextCompat.getDrawable(this, resId);
        setNavigationIcon(drawable);
    }

    protected void setNavigationIcon(Drawable drawable) {
        Integer color = null;

        try {
            TypedArray a = getTheme().obtainStyledAttributes(new int[]{R.attr.actionBarIconColor});
            color = a.getColor(0, Color.WHITE);
        } catch (Exception e) {
            L.ex(e);
        }
        if (color != null) {
            drawable.mutate().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }
        getActionBarToolbar().setNavigationIcon(drawable);
    }


}
