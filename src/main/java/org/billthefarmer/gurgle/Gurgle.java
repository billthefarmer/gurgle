////////////////////////////////////////////////////////////////////////////////
//
//  Gurgle - An android word game.
//
//  Copyright (C) 2022	Bill Farmer
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.
//
//  You should have received a copy of the GNU General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.
//
//  Bill Farmer	 william j farmer [at] yahoo [dot] co [dot] uk.
//
///////////////////////////////////////////////////////////////////////////////

package org.billthefarmer.gurgle;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import android.support.v4.content.FileProvider;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Gurgle class
public class Gurgle extends Activity
{
    public static final String TAG = "Gurgle";
    public static final String GURGLE_IMAGE = "Gurgle.png";
    public static final String IMAGE_PNG = "image/png";
    public static final String FILE_PROVIDER =
        "org.billthefarmer.gurgle.fileprovider";

    public static final int KEYBOARD[] =
    {
        R.id.keys1, R.id.keys2, R.id.keys3
    };

    public static final int ROWS[] =
    {
        R.id.row1, R.id.row2, R.id.row3, R.id.row4, R.id.row5, R.id.row6
    };

    private TextView display[][];
    private Map<String, TextView> keyboard;
    private Toast toast;
    private String word;
    private int letter;
    private int row;

    // On create
    @Override
    @SuppressWarnings("deprecation")
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        keyboard = new HashMap<String, TextView>();
        for (int id: KEYBOARD)
        {
            ViewGroup group = (ViewGroup) findViewById(id);
            for (int i = 0; i < group.getChildCount(); i++)
            {
                View view = group.getChildAt(i);
                if (view instanceof TextView)
                {
                    view.setOnClickListener((v) -> keyClicked(v));
                    keyboard.put(((TextView) view).getText().toString(),
                                 (TextView) view);
                }
            }
        }

        View view = findViewById(R.id.enter);
        view.setOnClickListener((v) -> enterClicked(v));
        view = findViewById(R.id.back);
        view.setOnClickListener((v) -> backspaceClicked(v));
        view = findViewById(R.id.gurgle);
        view.setOnClickListener((v) -> about());

        display = new TextView[ROWS.length][];
        int row = 0;
        for (int id: ROWS)
        {
            ViewGroup group = (ViewGroup) findViewById(id);
            display[row] = new TextView[group.getChildCount()];
            for (int i = 0; i < group.getChildCount(); i++)
                display[row][i] = (TextView) group.getChildAt(i);

            row++;
        }

        word = Words.getWord();
        letter = 0;
        row = 0;
    }

    // On create options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it
        // is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return true;
    }

    // On options item selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Get id
        int id = item.getItemId();
        switch (id)
        {
        case R.id.refresh:
            refresh();
            break;

        case R.id.share:
            share();
            break;

        case R.id.help:
            help();
            break;

        case R.id.about:
            about();
            break;

        default:
            return false;
        }

        return true;
    }

    // keyClicked
    public void keyClicked(View v)
    {
        if (letter < display[row].length)
        {
            CharSequence s = ((TextView)v).getText();
            display[row][letter++].setText(s);
        }

        else
            showToast(R.string.press);
    }

    // enterClicked
    public void enterClicked(View v)
    {
        if (letter != display[row].length)
        {
            showToast(R.string.finish);
            return;
        }

        StringBuilder guess = new StringBuilder();
        for (TextView t: display[row])
            guess.append(t.getText());

        if (!Words.isWord(guess.toString()))
        {
            showToast(R.string.not_list);
            return;
        }

        for (int i = 0; i < display[row].length; i++)
        {
            String wordLetter = word.substring(i, i + 1);
            String guessLetter = guess.substring(i, i + 1);
            TextView key = keyboard.get(guessLetter);

            if (wordLetter.contentEquals(guessLetter))
            {
                display[row][i].setTextColor(0xff00ff00);
                key.setTextColor(0xff00ff00);
            }

            else if (word.contains(guessLetter))
            {
                display[row][i].setTextColor(0xffffff00);
                if (key.getTextColors().getDefaultColor() != 0xff00ff00)
                    key.setTextColor(0xffffff00);
            }

            else
            {
                display[row][i].setTextColor(0x7fffffff);
                if (key.getTextColors().getDefaultColor() != 0xff00ff00)
                    key.setTextColor(0x7fffffff);
            }
        }

        letter = 0;
        row = (row + 1) % ROWS.length;
        for (TextView t: display[row])
        {
            t.setText("");
            t.setTextColor(0xffffffff);
        }
    }

    // backspaceClicked
    public void backspaceClicked(View v)
    {
        if (letter > 0)
            display[row][--letter].setText("");
    }

    // refresh
    private void refresh()
    {
        for (TextView r[]: display)
        {
            for (TextView t: r)
            {
                t.setText("");
                t.setTextColor(0xffffffff);
            }
        }

        for (TextView t: keyboard.values().toArray(new TextView[0]))
                t.setTextColor(0xffffffff);

        word = Words.getWord();
        letter = 0;
        row = 0;
    }

    // share
    @SuppressWarnings("deprecation")
    private void share()
    {
        Intent intent = new Intent(Intent.ACTION_SEND);
        String title = getString(R.string.app_name);
        intent.putExtra(Intent.EXTRA_TITLE, title);
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.setType(IMAGE_PNG);

        View root = findViewById(R.id.gurgle).getRootView();
        root.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(root.getDrawingCache());
        root.setDrawingCacheEnabled(false);

        File image = new File(getCacheDir(), GURGLE_IMAGE);
        try (BufferedOutputStream out = new
             BufferedOutputStream(new FileOutputStream(image)))
        {
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
        }

        catch (Exception e) {}

        Uri imageUri = FileProvider
            .getUriForFile(this, FILE_PROVIDER, image);
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        intent.putExtra(Intent.EXTRA_TEXT, word);
        startActivity(Intent.createChooser(intent, null));
    }

    // On help click
    private void help()
    {
        Intent intent = new Intent(this, Help.class);
        startActivity(intent);
    }

    // about
    @SuppressWarnings("deprecation")
    private boolean about()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.drawable.ic_launcher);

        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        SpannableStringBuilder spannable =
            new SpannableStringBuilder(getText(R.string.version));
        Pattern pattern = Pattern.compile("%s");
        Matcher matcher = pattern.matcher(spannable);
        if (matcher.find())
            spannable.replace(matcher.start(), matcher.end(),
                              BuildConfig.VERSION_NAME);
        matcher.reset(spannable);
        if (matcher.find())
            spannable.replace(matcher.start(), matcher.end(),
                              dateFormat.format(BuildConfig.BUILT));
        builder.setMessage(spannable);

        // Add the button
        builder.setPositiveButton(android.R.string.ok, null);

        // Create the AlertDialog
        Dialog dialog = builder.show();

        // Set movement method
        TextView text = dialog.findViewById(android.R.id.message);
        if (text != null)
        {
            text.setTextAppearance(builder.getContext(),
                                   android.R.style.TextAppearance_Small);
            text.setMovementMethod(LinkMovementMethod.getInstance());
        }

        return true;
    }

    // Show toast.
    void showToast(int key)
    {
        String text = getString(key);

        // Cancel the last one
        if (toast != null)
            toast.cancel();

        // Make a new one
        toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
