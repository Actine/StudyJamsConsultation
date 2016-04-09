package info.teib.newtest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * <p></p>
 *
 * @author Paul Danyliuk
 */
public class LandmarksAdapter extends RecyclerView.Adapter<LandmarkHolder> {

    private Context context;
    private Landmark[] data;

    public LandmarksAdapter(Context context, Landmark[] data) {
        super();
        this.context = context;
        this.data = data;
    }

    @Override
    public LandmarkHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(context).inflate(R.layout.item_landmark, parent, false);
        return new LandmarkHolder(context, itemView);
    }

    @Override
    public void onBindViewHolder(LandmarkHolder holder, int position) {
        holder.setNewData(data[position]);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

}
