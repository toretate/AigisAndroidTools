package com.toretate.aigisandroidtools.pager;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewPagerContentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewPagerContentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewPagerContentFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";

	// TODO: Rename and change types of parameters
	private int m_position;

	private OnFragmentInteractionListener mListener;

	public static ViewPagerContentFragment newInstance( int position ) {
		ViewPagerContentFragment fragment = new ViewPagerContentFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_PARAM1, position);
		fragment.setArguments(args);
		return fragment;
	}


	public ViewPagerContentFragment() {
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment

		ViewPagerPageDefs pageDefs = ViewPagerPageDefs.getInstance(getActivity());
		return pageDefs.createView( m_position, getActivity(), inflater, container );
	}

	@Override
	public void onDestroyView() {
		ViewPagerPageDefs pageDefs = ViewPagerPageDefs.getInstance(getActivity());
		pageDefs.destroyView( m_position );
		super.onDestroyView();
	}

	@Override
	public void onAttach( Context context ) {
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
