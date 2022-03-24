package club.devsoc.scanf.view.fragment

import android.content.ContentUris
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import club.devsoc.scanf.R
import club.devsoc.scanf.databinding.HomeFragmentBinding
import club.devsoc.scanf.model.GalleryModel
//import club.devsoc.scanf.view.activity.GalleryActivity
import club.devsoc.scanf.view.adapter.GalleryAdapter
import club.devsoc.scanf.viewmodel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.gallery_bottom_sheet.*
import kotlinx.android.synthetic.main.gallery_bottom_sheet.view.*
import kotlinx.android.synthetic.main.home_fragment.*

private lateinit var bottomSheetBehavior :
        BottomSheetBehavior<ConstraintLayout>

private lateinit var photoList : ArrayList<GalleryModel>



class HomeFragment : Fragment() {
    lateinit var binding: HomeFragmentBinding
    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        startCamera()
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        // TODO: Use the ViewModel
        val extras = FragmentNavigatorExtras(binding.selectedDocIv to "rd_image_big")
        binding.selectedDocIv.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_recentDocumentFragment,
            null,
            null,
            extras)
        }
        binding.galleryBtn.setOnClickListener{ view ->

            openGallery()
        }

        binding.root.gallery_exit.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }


        getImages() // Gets all image URIs from gallery

        setGallery() // sets the recyclerview for gallery
        bottomSheet() // Initializes bottom sheet

    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewFinder.surfaceProvider)
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview
                )

            } catch (exc: Exception) {
                Log.e("HomeFragment", "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun setGallery() {

        var galleryAdapter = GalleryAdapter(photoList)
        gallery_view.layoutManager=GridLayoutManager(activity, 2)
        gallery_view.adapter = galleryAdapter
        //notify data set changed
        galleryAdapter.notifyDataSetChanged()
    }

    private fun openGallery() {

        val view = layoutInflater.inflate(R.layout.gallery_bottom_sheet, null)

        view.gallery_exit.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        if (bottomSheetBehavior.state == 2 ||
            bottomSheetBehavior.state == 5)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        else
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

    }

    private fun bottomSheet() {

        bottomSheetBehavior = BottomSheetBehavior.from(gallery_layout)

        // setting initial state of bottom sheet to hidden
        bottomSheetBehavior.isHideable = true;
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

    }

    private fun getImages() {
        val imageProjection = arrayOf(
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media._ID
        )
        val imageSortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"
        val contentResolver = activity?.contentResolver
        val cursor = contentResolver?.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            imageProjection,
            null,
            null,
            imageSortOrder
        )

        photoList = ArrayList(3)
        if(cursor != null && cursor.count != 0) {

            cursor.moveToNext()
            while(cursor.isAfterLast == false) {
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val id = cursor.getLong(idColumn)
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                photoList.add(GalleryModel(contentUri))
                cursor.moveToNext()


            }
        }

    }


}