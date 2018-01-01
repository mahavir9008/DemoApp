package demo.payconiq.com.payconiq;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import demo.payconiq.com.payconiq.model.Repository;
import demo.payconiq.com.payconiq.utility.Constant;

import static demo.payconiq.com.payconiq.utility.Constant.ITEM;
import static demo.payconiq.com.payconiq.utility.Constant.LOADING;


public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Repository> repoData;
    private boolean isLoadingAdded = false;
    public PaginationAdapter() {
        repoData = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.item_list, parent, false);
        viewHolder = new RepoVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Repository result = repoData.get(position);

        switch (getItemViewType(position)) {
            case ITEM:
                final RepoVH repoVH = (RepoVH) holder;
                repoVH.mName.setText(result.getFullName());
                repoVH.forks.setText(Constant.FORK_TEXT + result.getForks());
                repoVH.mDesc.setText(result.getDescription());
                break;
        }

    }

    @Override
    public int getItemCount() {
        return repoData.size();

    }

    @Override
    public int getItemViewType(int position) {
        return (position == repoData.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    /*
   Helpers
   _________________________________________________________________________________________________
    */

    private void add(Repository r) {
        repoData.add(r);
        notifyItemInserted(repoData.size() - 1);
    }

    public void addAll(List<Repository> moveResults) {
        for (Repository result : moveResults) {
            add(result);
        }
    }

    public void stopLoading() {
        isLoadingAdded = false;
        notifyDataSetChanged();
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Repository());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = repoData.size() - 1;
        Repository result = getItem(position);
        if (result != null) {
            repoData.remove(position);
            notifyItemRemoved(position);
        }
    }

    private Repository getItem(int position) {
        return repoData.get(position);
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    class RepoVH extends RecyclerView.ViewHolder {
        private final TextView mName;
        private final TextView mDesc;
        private final TextView forks;

        public RepoVH(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.userName);
            mDesc = (TextView) itemView.findViewById(R.id.userDesc);
            forks = (TextView) itemView.findViewById(R.id.forks);
        }
    }

    class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }


}
