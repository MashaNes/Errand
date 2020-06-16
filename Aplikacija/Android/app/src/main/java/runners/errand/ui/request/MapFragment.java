package runners.errand.ui.request;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.VectorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import runners.errand.MainActivity;
import runners.errand.R;
import runners.errand.model.Request;
import runners.errand.model.Task;
import runners.errand.utils.BitmapUtils;

public class MapFragment extends Fragment implements OnMapReadyCallback {
	private MapView mapView;
	private GoogleMap map;
	private RequestFragment parent;
	private MainActivity activity;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_map, container, false);

		parent = ((RequestFragment) getParentFragment());
		if (parent == null) return root;

		activity = parent.getMainActivity();

		mapView = root.findViewById(R.id.request_map_view);
		mapView.onCreate(savedInstanceState);
		mapView.getMapAsync(this);

		return root;
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		map = googleMap;

		// Marker icon
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		BitmapDescriptor icon_red = BitmapUtils.getMapMarkerBitmap(activity, metrics.density, BitmapUtils.MARKER_ICON_RED);
		BitmapDescriptor icon_green = BitmapUtils.getMapMarkerBitmap(activity, metrics.density, BitmapUtils.MARKER_ICON_GREEN);

		// Add markers for all addresses in request
		LatLngBounds.Builder builder = new LatLngBounds.Builder();
		Request request = parent.getRequest();
		LatLng latLng;
		latLng = new LatLng(request.getDestination().getLat(), request.getDestination().getLng());
		map.addMarker(new MarkerOptions().icon(request.getDestination().isArrived() ? icon_green : icon_red).position(latLng).title(getString(R.string.newrequest_info_destination)).snippet(request.getDestination().getName()));
		builder.include(latLng);
		for (Task task : request.getTasks()) {
			if (task.getAddress() != null) {
				latLng = new LatLng(task.getAddress().getLat(), task.getAddress().getLng());
				map.addMarker(new MarkerOptions().icon(task.getAddress().isArrived() ? icon_green : icon_red).position(latLng).title(task.getName()).snippet(task.getAddress().getName()));
				builder.include(latLng);
			}
		}

		// Zoom
		CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(builder.build(), 200);
		map.moveCamera(cu);
		map.setMaxZoomPreference(16f);
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void onResume() {
		mapView.onResume();
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		mapView.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mapView.onLowMemory();
	}
}
