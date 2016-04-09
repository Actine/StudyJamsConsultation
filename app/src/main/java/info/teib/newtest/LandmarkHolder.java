package info.teib.newtest;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * <p></p>
 *
 * @author Paul Danyliuk
 */
public class LandmarkHolder extends RecyclerView.ViewHolder {

    private Context context;
    private ImageView picture;
    private TextView title;
    private Button like;
    private Landmark landmark;

    public LandmarkHolder(final Context context, View itemView) {
        super(itemView);
        this.context = context;
        picture = (ImageView) itemView.findViewById(R.id.picture);
        title = (TextView) itemView.findViewById(R.id.title);
        like = (Button) itemView.findViewById(R.id.like);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (landmark != null) {
                    landmark.isViewed = true;
                    like.setEnabled(false);
                    like.setText("Переглянуто");

                    PreferenceManager.getDefaultSharedPreferences(context)
                            .edit().putInt("lastViewed", landmark.index).apply();

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://google.com/search?q=" + landmark.title));
                    context.startActivity(intent);
                }
            }
        });
    }

    public void setNewData(Landmark landmark) {
        this.landmark = landmark;

        this.picture.setImageDrawable(
                context.getResources().getDrawable(landmark.pictureId)
        );
        this.title.setText(landmark.title);
        this.like.setEnabled(!landmark.isViewed);
        if (landmark.isViewed) {
            like.setText("Переглянуто");
        }
    }

}
