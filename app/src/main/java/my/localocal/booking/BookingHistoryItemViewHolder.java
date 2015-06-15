package my.localocal.booking;

import my.localocal.circularimageview.RoundedImageView;
import android.widget.ImageView;
import android.widget.TextView;

public class BookingHistoryItemViewHolder{

	private RoundedImageView	img_thumbnail;
	private TextView	     lbl_place, lbl_tourDateTitle, lbl_tourDate, lbl_statusTitle, lbl_status;

	public BookingHistoryItemViewHolder() {
		//...
	}

	public void setBookingHistoryThumbnailImageView(RoundedImageView imageView) {
		img_thumbnail = imageView;
	}

	public void setBookingHistoryPlaceTextView(TextView textView) {
		lbl_place = textView;
	}

	public void setBookingHistoryDateTitleTextView(TextView textView) {
		lbl_tourDateTitle = textView;
	}

	public void setBookingHistoryDateTextView(TextView textView) {
		lbl_tourDate = textView;
	}

	public void setBookingHistoryStatusTitleTextView(TextView textView) {
		lbl_statusTitle = textView;
	}

	public void setBookingHistoryStatusTextView(TextView textView) {
		lbl_status = textView;
	}

	public ImageView getBookingHistoryThumbnailImageView() {
		return img_thumbnail;
	}

	public TextView getBookingHistoryPlaceTextView() {
		return lbl_place;
	}

	public TextView getBookingHistoryDateTitleTextView() {
		return lbl_tourDateTitle;
	}

	public TextView getBookingHistoryDateTextView() {
		return lbl_tourDate;
	}

	public TextView getBookingHistoryStatusTitleTextView() {
		return lbl_statusTitle;
	}

	public TextView getBookingHistoryStatusTextView() {
		return lbl_status;
	}

}
