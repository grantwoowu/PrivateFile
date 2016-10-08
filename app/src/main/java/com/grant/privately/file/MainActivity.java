package com.grant.privately.file;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.grant.privately.file.common.Constant;
import com.grant.privately.file.fragment.FileItemFragment;
import com.grant.privately.file.fragment.dummy.MediaPathEntry;
import com.grant.privately.file.fragment.dummy.MediaType;

import java.io.File;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,FileItemFragment.OnListFragmentInteractionListener {

    private FileItemFragment fileItemFragment;
    private Menu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_checked_all) {
            fileItemFragment.setOptionId(Constant.OPTION_ID_CHECKED_ALL);
            return true;
        }
        if (id == R.id.action_checked_invert) {
            fileItemFragment.setOptionId(Constant.OPTION_ID_CHECKED_INVERT);
            return true;
        }
        if (id == R.id.action_checked_cancel) {
            fileItemFragment.setOptionId(Constant.OPTION_ID_CHECKED_HIDDEN);
            return true;
        }
        if (id == R.id.action_decode) {
            fileItemFragment.setOptionId(Constant.OPTION_ID_DECODE);
            return true;
        }
        if (id == R.id.action_encode) {
            fileItemFragment.setOptionId(Constant.OPTION_ID_ENCODE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        int tabId = 0;
        MediaType listId;
        switch (id){
            case R.id.nav_encode_image:
                tabId = 0;
                listId = MediaType.IMAGE;
                break;
            case R.id.nav_encode_video:
                tabId = 0;
                listId = MediaType.VIDEO;
                break;
            case R.id.nav_encode_voice:
                tabId = 0;
                listId = MediaType.VOICE;
                break;
            case R.id.nav_uncode_image:
                tabId = 1;
                listId = MediaType.IMAGE;
                break;
            case R.id.nav_uncode_video:
                tabId = 1;
                listId = MediaType.VIDEO;
                break;
            case R.id.nav_uncode_voice:
                tabId = 1;
                listId = MediaType.VOICE;
                break;
            default:
                tabId = 0;
                listId = MediaType.IMAGE;
                break;

        }

        menu.clear();

        if (tabId == 1){
            getMenuInflater().inflate(R.menu.main_encode, menu);
        }else{
            getMenuInflater().inflate(R.menu.main_decode, menu);
        }

        //Toast.makeText(this, "点击"+tabId+","+listId, Toast.LENGTH_SHORT).show();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        fileItemFragment = FileItemFragment.newInstance(tabId,listId);
        transaction.replace(R.id.content_main, fileItemFragment);
        //transaction.addToBackStack(null);
        transaction.commit();


        return true;
    }

    @Override
    public void onListFragmentInteraction(MediaPathEntry item) {

        try {
            switch (item.getMediaType()){
                case IMAGE:
                    Intent imageIntent =new Intent(Intent.ACTION_VIEW);
                    imageIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    imageIntent.setDataAndType(Uri.parse(item.getImgPath()), "image/*");
                    startActivity(imageIntent);
                    break;
                case VIDEO:
                    startActivity(getVideoFileIntent(new File(item.getImgPath())));
                    break;
                case VOICE:
                    Intent voiceIntent =new Intent(Intent.ACTION_VIEW);
                    voiceIntent.setDataAndType(Uri.parse(item.getImgPath()), "audio/*");
                    startActivity(voiceIntent);
                    break;
            }
        }catch (ActivityNotFoundException exception){
            exception.printStackTrace();
            Toast.makeText(this,"请安装可以浏览图片、播放视频、语音的软件",Toast.LENGTH_LONG).show();
        }


    }

    /***
     * 调用系统播放器播放视频
     * @param videoFile
     */
    private Intent getVideoFileIntent(File videoFile)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(videoFile);
        intent.setDataAndType(uri, "video/*");
        return intent;
    }
}
