package my.localocal.booking;

import java.util.Calendar;

import my.localocal.DataManager;
import my.localocal.R;
import my.localocal.circularimageview.RoundedImageView;
import my.localocal.data.BookingItem;
import my.localocal.data.Place;
import my.localocal.theme.ThemeHandler;
import my.localocal.views.listview.ListViewItem;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class BookingHistoryItemView implements ListViewItem{

	private final Context	   context;
	private final BookingItem	item;
	private final ThemeHandler	themeHandler;
	private final Place	       place;

	public BookingHistoryItemView(Context context, BookingItem item) {
		this.context = context;
		this.item = item;
		themeHandler = new ThemeHandler(context);
		place = DataManager.shared(context).getPlaceById(item.getPlaceId());

	}

	@Override
	public View createView() {
		View view = LayoutInflater.from(context).inflate(R.layout.booking_history_item, null);

		BookingHistoryItemViewHolder holder = new BookingHistoryItemViewHolder();

		holder.setBookingHistoryThumbnailImageView((RoundedImageView) view.findViewById(R.id.booking_history_item__img_thumbnail));
		holder.setBookingHistoryPlaceTextView((TextView) view.findViewById(R.id.booking_history_item__lbl_place));
		holder.setBookingHistoryDateTitleTextView((TextView) view.findViewById(R.id.booking_history_item__lbl_tourdateTitle));
		holder.setBookingHistoryDateTextView((TextView) view.findViewById(R.id.booking_history_item__lbl_tourdate));
		holder.setBookingHistoryStatusTitleTextView((TextView) view.findViewById(R.id.booking_history_item__lbl_statusTitle));
		holder.setBookingHistoryStatusTextView((TextView) view.findViewById(R.id.booking_history_item__lbl_status));

		setValueToItemViewHolder(holder);
		view.setTag(holder);
		return view;
	}

	private void setValueToItemViewHolder(BookingHistoryItemViewHolder holder) {

		holder.getBookingHistoryThumbnailImageView().setImageDrawable(themeHandler.getImageDrawable(place.getPlaceImages().get(0)));

		holder.getBookingHistoryPlaceTextView().setText(place.getPlaceName());

		holder.getBookingHistoryDateTitleTextView().setText("Tour Date :");
		holder.getBookingHistoryDateTextView().setText(item.getTourDate().get(Calendar.YEAR) + "/" + (item.getTourDate().get(Calendar.MONTH) + 1) + "/" + item.getTourDate().get(Calendar.DATE));

		holder.getBookingHistoryStatusTitleTextView().setText("Booking Status :");
		holder.getBookingHistoryStatusTextView().setText(item.getStatus());

	}

	@Override
	public void convertView(View view) {
		BookingHistoryItemViewHolder holder = (BookingHistoryItemViewHolder) view.getTag();
		setValueToItemViewHolder(holder);
	}

	@Override
	public boolean isConvertable(View view) {
		return view.getTag() instanceof BookingHistoryItemViewHolder;
	}

}
