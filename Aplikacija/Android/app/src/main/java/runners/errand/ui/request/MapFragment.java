package runners.errand.ui.request;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import runners.errand.MainActivity;
import runners.errand.R;
import runners.errand.model.Request;
import runners.errand.model.Task;
import runners.errand.utils.ImageUtils;

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
		loadData();
	}

	void loadData() {
		if (map == null) return;
		map.clear();

		// Marker icon
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		BitmapDescriptor icon_red = ImageUtils.getMapMarkerBitmap(activity, metrics.density, ImageUtils.MARKER_ICON_RED);
		BitmapDescriptor icon_green = ImageUtils.getMapMarkerBitmap(activity, metrics.density, ImageUtils.MARKER_ICON_GREEN);

		// Add markers for all addresses in request
		LatLngBounds.Builder builder = new LatLngBounds.Builder();
		Request request = parent.getRequest();
		LatLng latLng;
		boolean atLeastOne = false;
		if (request.getDestination() != null) {
			latLng = new LatLng(request.getDestination().getLat(), request.getDestination().getLng());
			map.addMarker(new MarkerOptions().icon(request.getDestination().isArrived() ? icon_green : icon_red).position(latLng).title(getString(R.string.newrequest_info_destination)).snippet(request.getDestination().getName()));
			builder.include(latLng);
			atLeastOne = true;
		}
		for (Task task : request.getTasks()) {
			if (task.getAddress() != null) {
				latLng = new LatLng(task.getAddress().getLat(), task.getAddress().getLng());
				map.addMarker(new MarkerOptions().icon(task.getAddress().isArrived() ? icon_green : icon_red).position(latLng).title(task.getName()).snippet(task.getAddress().getName()));
				builder.include(latLng);
				atLeastOne = true;
			}
		}

		// Zoom
		if (!atLeastOne) return;
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
