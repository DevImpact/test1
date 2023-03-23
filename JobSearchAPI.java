

public class JobSearchAPI {
    private static final String BASE_URL = "https://api.indeed.com/ads/apisearch";
    private static final String KEY = "YOUR_INDEED_API_KEY";

    private static Retrofit retrofit = null;

    private static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static void searchJobs(String searchTerm, String field, String location, String hours, final Callback<List<JobResult>> callback) {
        JobSearchService jobSearchService = getRetrofitInstance().create(JobSearchService.class);
        Call<JobSearchResponse> call = jobSearchService.searchJobs(KEY, searchTerm, field, location, hours);
        call.enqueue(new retrofit2.Callback<JobSearchResponse>() {
            @Override
            public void onResponse(Call<JobSearchResponse> call, Response<JobSearchResponse> response) {
                List<JobResult> jobResults = response.body().getJobResults();
                callback.onResponse(jobResults);
            }

            @Override
            public void onFailure(Call<JobSearchResponse> call, Throwable t) {
                callback.onFailure(new Exception(t));
            }
        });
    }
}