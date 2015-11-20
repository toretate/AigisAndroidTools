package layout;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.toretate.aigistwclient.R;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.Timeline;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TabFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";

	// TODO: Rename and change types of parameters
	private int m_position;

	private OnFragmentInteractionListener mListener;

	public static TabFragment newInstance( int position ) {
		TabFragment fragment = new TabFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_PARAM1, position);
		fragment.setArguments(args);
		return fragment;
	}


	public TabFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			m_position = getArguments().getInt(ARG_PARAM1);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_tab, container, false);

		Timeline<Tweet> timeline;
		if( m_position == 1 ) {
			timeline = new UserTimeline.Builder()
					.includeReplies(false)
					.includeRetweets(false)
					.maxItemsPerRequest(5)
					.screenName("Aigis1000")
					.build();
		} else {
			timeline = new SearchTimeline.Builder()
					.query("#千年戦争アイギス")
					.maxItemsPerRequest(5)
					.languageCode("ja")
					.build();
		}

		final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder( getActivity() )
				.setTimeline( timeline )
				.build();

		ListView twitter = (ListView)view.findViewById( R.id.twitter );
		twitter.setAdapter( adapter );

		final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById( R.id.swipeRefresh );
		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				swipeRefreshLayout.setRefreshing( true );
				adapter.refresh(new Callback<TimelineResult<Tweet>>() {
					@Override
					public void success(Result<TimelineResult<Tweet>> result) {
						swipeRefreshLayout.setRefreshing( false );
					}

					@Override
					public void failure(TwitterException e) {
						// 失敗通知(Toastとかで？)
					}
				});
			}
		});

		return view;
	}

	// TODO: Rename method, update argument and hook method into UI event
//	public void onButtonPressed(Uri uri) {
//		if (mListener != null) {
//			mListener.onFragmentInteraction(uri);
//		}
//	}

	@Override
	public void onAttach(Activity context) {
		super.onAttach(context);
		if (context instanceof OnFragmentInteractionListener) {
			mListener = (OnFragmentInteractionListener) context;
		} else {
			throw new RuntimeException(context.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated
	 * to the activity and potentially other fragments contained in that
	 * activity.
	 * <p/>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		void onFragmentInteraction(Uri uri);
	}
}
