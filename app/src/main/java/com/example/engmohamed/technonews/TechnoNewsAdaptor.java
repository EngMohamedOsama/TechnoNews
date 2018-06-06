package com.example.engmohamed.technonews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * An {@link TechnoNewsAdaptor} knows how to create a list item layout for each News item
 * in the data source (a list of {@link TechnoNewsAdaptor} objects).
 * <p>
 * These list item layouts will be provided to an adapter view like ListView
 * to be displayed to the user.
 */
public class TechnoNewsAdaptor extends ArrayAdapter<TechnoNews> {

    /**
     * Constructs a new {@link TechnoNewsAdaptor}.
     *
     * @param context of the app
     * @param data    is the list of TechnoNews, which is the data source of the adapter
     */
    TechnoNewsAdaptor(@NonNull Context context, @NonNull ArrayList<TechnoNews> data) {
        super(context, 0, data);
    }

    /**
     * Returns a list item view that displays information about the Technology News at the given position
     * in the list of TechnologyNews.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_style, parent, false);
        }

        // Find the News at the given position in the list of Technology News
        TechnoNews currentItem = getItem(position);

        // Find a reference to item image
        ImageView itemImage = convertView.findViewById(R.id.item_image);

        // Set the item image
        itemImage.setImageDrawable(currentItem.getImage());

        // Find a reference to item title
        TextView itemTitle = convertView.findViewById(R.id.item_title);

        // Set the item title
        itemTitle.setText(currentItem.getTitle());

        // Find a reference to item description
        TextView itemDescription = convertView.findViewById(R.id.item_description);

        // Set the item description
        itemDescription.setText(currentItem.getDescription());

        // Find a reference to item author
        TextView itemAuthor = convertView.findViewById(R.id.item_author);

        // Set the item author
        itemAuthor.setText(currentItem.getAuthor());

        // Find a reference to item date
        TextView itemDate = convertView.findViewById(R.id.item_date);

        // Set the item date and separate the time
        itemDate.setText(currentItem.getDate().substring(0, 10));

        // Find a reference to item section
        TextView itemSection = convertView.findViewById(R.id.item_section);

        // Set item section
        itemSection.setText(currentItem.getSection());

        // Return the list item view that is now showing the appropriate data
        return convertView;
    }
}
