


public class JobResultAdapter extends RecyclerView.Adapter<JobResultAdapter.JobViewHolder> {
    private List<JobResult> jobResults;

    public JobResultAdapter(List<JobResult> jobResults) {
        this.jobResults = jobResults;
    }

    @Override
    public JobViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_result_item, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(JobViewHolder holder, int position) {
        JobResult jobResult = jobResults.get(position);
        holder.jobTitleTextView.setText(jobResult.getJobTitle());
        holder.companyNameTextView.setText(jobResult.getCompanyName());
        holder.locationTextView.setText(jobResult.getLocation());
        holder.datePostedTextView.setText(jobResult.getDatePosted());
    }

    @Override
    public int getItemCount() {
        return jobResults.size();
    }

    static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView jobTitleTextView;
        TextView companyNameTextView;
        TextView locationTextView;
        TextView datePostedTextView;

        JobViewHolder(View itemView) {
            super(itemView);
            jobTitleTextView = itemView.findViewById(R.id.job_title_text_view);
            companyNameTextView = itemView.findViewById(R.id.company_name_text_view);
            locationTextView = itemView.findViewById(R.id.location_text_view);
            datePostedTextView = itemView.findViewById(R.id.date_posted_text_view);
        }
    }
}