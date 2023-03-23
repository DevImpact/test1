

public class JobSearchActivity extends AppCompatActivity {
    private EditText searchEditText;
    private Spinner fieldSpinner;
    private Spinner locationSpinner;
    private Spinner hoursSpinner;
    private RecyclerView jobRecyclerView;
    private JobResultAdapter jobResultAdapter;
    private List<JobResult> jobResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_search);

        searchEditText = findViewById(R.id.search_box);
        fieldSpinner = findViewById(R.id.field_spinner);
        locationSpinner = findViewById(R.id.location_spinner);
        hoursSpinner = findViewById(R.id.hours_spinner);
        jobRecyclerView = findViewById(R.id.job_list);

        jobResults = new ArrayList<>();
        jobResultAdapter = new JobResultAdapter(jobResults);
        jobRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        jobRecyclerView.setAdapter(jobResultAdapter);

        Button searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchTerm = searchEditText.getText().toString();
                String field = fieldSpinner.getSelectedItem().toString();
                String location = locationSpinner.getSelectedItem().toString();
                String hours = hoursSpinner.getSelectedItem().toString();

                JobSearchAPI.searchJobs(searchTerm, field, location, hours, new Callback<List<JobResult>>() {
                    @Override
                    public void onResponse(List<JobResult> jobResults) {
                        jobResults.clear();
                        jobResults.addAll(jobResults);
                        jobResultAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(JobSearchActivity.this, "Failed to search for jobs", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}