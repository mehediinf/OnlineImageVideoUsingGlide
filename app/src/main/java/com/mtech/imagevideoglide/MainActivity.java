package com.mtech.imagevideoglide;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer3.ExoPlayer;
import com.google.android.exoplayer3.MediaItem;
import com.google.android.exoplayer3.ui.PlayerView;

import java.io.IOException;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView, videoThumbnail;
    private PlayerView playerView;
    private ExoPlayer exoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        videoThumbnail = findViewById(R.id.videoThumbnail);
        playerView = findViewById(R.id.playerView);

        String videoUrl = "https://videos.pexels.com/video-files/4114413/4114413-hd_1920_1080_25fps.mp4";
        String imageUrl = "https://images.rawpixel.com/image_800/czNmcy1wcml2YXRlL3Jhd3BpeGVsX2ltYWdlcy93ZWJzaXRlX2NvbnRlbnQvbHIvcHUyMzMxNjM2LWltYWdlLWt3dnk3dzV3LmpwZw.jpg";

        // Glide দিয়ে ইমেজ লোড
        Glide.with(this)
                .load(imageUrl)
                .into(imageView);

        // ভিডিও থাম্বনেইল তৈরি করা
        Bitmap bitmap = getVideoThumbnail(videoUrl);

        // Glide দিয়ে থাম্বনেইল লোড করা
        if (bitmap != null) {
            Glide.with(this)
                    .load(bitmap)
                    .into(videoThumbnail);
        }

        // ExoPlayer তৈরি এবং ভিডিও প্লে করা
        exoPlayer = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(exoPlayer);

        // ভিডিও URL সেট করা এবং প্লে করা
        MediaItem mediaItem = MediaItem.fromUri(videoUrl);
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.play();
    }

    // ভিডিও থাম্বনেইল তৈরি করার ফাংশন
    private Bitmap getVideoThumbnail(String videoUrl) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(videoUrl, new HashMap<String, String>());
            return retriever.getFrameAtTime(1000000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC); // 1 সেকেন্ডের মধ্যে প্রথম ফ্রেম
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                retriever.release();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // ভিডিও পজ করা
        exoPlayer.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // ExoPlayer রিলিজ করা
        exoPlayer.release();
    }
}
