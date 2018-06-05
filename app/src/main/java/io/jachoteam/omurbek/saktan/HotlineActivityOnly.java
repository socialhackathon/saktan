package io.jachoteam.omurbek.saktan;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HotlineActivityOnly extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotline_only);

        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);

        // Defined Array values to show in ListView
        List<HotlineModel> hotlineModels = new ArrayList<>();
        hotlineModels.add(new HotlineModel("Пожарная служба", "101"));
        hotlineModels.add(new HotlineModel("Милиция", "102"));
        hotlineModels.add(new HotlineModel("Скорая помощь", "103"));
        hotlineModels.add(new HotlineModel("МЧС", "161"));
        hotlineModels.add(new HotlineModel("Детская поддержка", "312611552"));
        hotlineModels.add(new HotlineModel("ГРС", "663605"));
        hotlineModels.add(new HotlineModel("Социальный фонд", "0312543316"));

        HotlineListViewAdapter adapter = new HotlineListViewAdapter(this,
                R.layout.hotline_list, hotlineModels);

        // Assign adapter to ListView
        listView.setAdapter(adapter);

    }

    public void phoneToDialler(View view) {

        TextView textView = view.findViewById(R.id.myname);
        TextView phoneView = view.findViewById(R.id.phone);

        String phone = String.valueOf(phoneView.getText());

        Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts(
                "tel", phone, null));
        startActivity(phoneIntent);
    }
}
