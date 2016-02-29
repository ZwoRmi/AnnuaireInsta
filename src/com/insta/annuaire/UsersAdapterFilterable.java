package com.insta.annuaire;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.insta.annuaire.pojo.Globals;
import com.insta.annuaire.pojo.UserResponse;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UsersAdapterFilterable extends ArrayAdapter<UserResponse>
		implements Filterable {

	private List<UserResponse> allUserResponseItemsArray;
	private List<UserResponse> filteredUserResponseItemsArray;
	private Activity context;
	private UserResponseFilter filter;

	public UsersAdapterFilterable(Activity context, List<UserResponse> list) {
		super(context, R.layout.user, list);
		this.context = context;
		this.allUserResponseItemsArray = new ArrayList<UserResponse>();
		allUserResponseItemsArray.addAll(list);
		Collections.sort(allUserResponseItemsArray);
		this.filteredUserResponseItemsArray = new ArrayList<UserResponse>();
		filteredUserResponseItemsArray.addAll(allUserResponseItemsArray);
		context.getLayoutInflater();
		getFilter();
	}

	public void resetData() {
		filteredUserResponseItemsArray.clear();
		filteredUserResponseItemsArray.addAll(allUserResponseItemsArray);
	}

	@Override
	public Filter getFilter() {
		if (filter == null) {
			filter = new UserResponseFilter();
		}
		return filter;
	}

	static class ViewHolder {
		protected ImageView picture;
		protected TextView firstName;
		protected TextView lastName;
		protected TextView promoNumber;
		protected TextView promoTitle;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Intent intent;
		intent = new Intent(this.context, UserActivity.class);
		View rowView = convertView;
		// reuse views
		if (rowView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.user, null);
			// configure view holder
			ViewHolder viewHolder = new ViewHolder();

			viewHolder.picture = (ImageView) rowView
					.findViewById(R.id.userPicture);
			viewHolder.firstName = (TextView) rowView
					.findViewById(R.id.firstName);
			viewHolder.lastName = (TextView) rowView
					.findViewById(R.id.lastName);
			viewHolder.promoNumber = (TextView) rowView
					.findViewById(R.id.promoNumber);
			viewHolder.promoTitle = (TextView) rowView
					.findViewById(R.id.promoTitle);
			rowView.setTag(viewHolder);
		}

		// fill data
		final ViewHolder holder = (ViewHolder) rowView.getTag();
		final UserResponse user = this.filteredUserResponseItemsArray
				.get(position);

		holder.firstName.setText(user.getFirstName());
		holder.lastName.setText(user.getName());
		holder.promoTitle.setText(user.getPromoTitle());
		holder.promoNumber.setText(context.getResources().getString(R.string.promo) + user.getPromoNumber());

		String urlPicture = user.getPicture();
		this.setPicture(urlPicture, holder);
		rowView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Globals.user = user;
				try {
					Globals.picture = ((BitmapDrawable) holder.picture
							.getDrawable()).getBitmap();
					context.startActivity(intent);
				} catch (NullPointerException e) {
					Toast.makeText(
							context,
							"Patientez quelques instants pendant le chargement des données",
							Toast.LENGTH_LONG).show();
				}
			}
		});

		return rowView;
	}

	private void setPicture(String urlPicture, ViewHolder holder) {
		if (urlPicture == null || urlPicture == "" || urlPicture == "null") {
			holder.picture.setImageResource(R.drawable.unknown);
		} else {
			Picasso.with(context).load(urlPicture).into(holder.picture);
		}
	}

	private class UserResponseFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {

			constraint = constraint.toString().toLowerCase();
			FilterResults result = new FilterResults();
			if (constraint != null && constraint.toString().length() > 0) {
				ArrayList<UserResponse> filteredItems = this
						.getFilteredItems(constraint);
				result.count = filteredItems.size();
				result.values = filteredItems;
			} else {
				synchronized (this) {
					result.values = allUserResponseItemsArray;
					result.count = allUserResponseItemsArray.size();
				}
			}
			return result;
		}

		private ArrayList<UserResponse> getFilteredItems(CharSequence constraint) {
			ArrayList<UserResponse> filteredItems = new ArrayList<UserResponse>();
			String[] constraints = constraint.toString().split(" ");
			ArrayList<ArrayList<UserResponse>> lists = new ArrayList<ArrayList<UserResponse>>();
			for (int i = 0; i < constraints.length; i++) {
				lists.add(getMatchingItems(constraints[i]));
			}
			filteredItems = lists.get(0);
			for (int i = 1; i < lists.size(); i++) {
				filteredItems = intersection(filteredItems, lists.get(i));
			}

			return filteredItems;
		}

		private ArrayList<UserResponse> getMatchingItems(String constraint) {
			ArrayList<UserResponse> result = new ArrayList<UserResponse>();
			for (int i = 0, l = allUserResponseItemsArray.size(); i < l; i++) {
				UserResponse user = allUserResponseItemsArray.get(i);
				if (!result.contains(user)
						&& user.getName().toLowerCase().contains(constraint))
					result.add(user);
				else if (user.getFirstName().toLowerCase().contains(constraint))
					result.add(user);
				else if (user.getPromoNumber().toLowerCase()
						.contains(constraint))
					result.add(user);
				else if (user.getPromoTitle().toLowerCase()
						.contains(constraint))
					result.add(user);
			}
			return result;
		}

		public <T> ArrayList<T> intersection(List<T> list1, List<T> list2) {
			ArrayList<T> list = new ArrayList<T>();
			for (T t : list1) {
				if (list2.contains(t)) {
					list.add(t);
				}
			}
			return list;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {

			filteredUserResponseItemsArray = (ArrayList<UserResponse>) results.values;
			notifyDataSetChanged();
			clear();
			for (int i = 0, l = filteredUserResponseItemsArray.size(); i < l; i++)
				add(filteredUserResponseItemsArray.get(i));
			notifyDataSetInvalidated();
		}
	}
}
