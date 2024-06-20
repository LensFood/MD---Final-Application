package com.example.lensfood1



import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lensfood1.databinding.FragmentBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetF : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetBinding? = null
    private val binding get() = _binding!!

    private var dataList = arrayListOf<DataCam>()
    private var imageBitmap: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get food image from arguments and display it in previewImageView
        imageBitmap = arguments?.getParcelable(ARG_IMAGE_BITMAP)
        imageBitmap?.let {
            binding.previewImageView.setImageBitmap(it)
        }

        // Get data list from arguments and populate RecyclerView
        dataList.clear()
        dataList.addAll(arguments?.getParcelableArrayList(ARG_DATA_LIST) ?: emptyList())
        setupRecyclerView()

        binding.cancelButton.setOnClickListener { dismiss() }
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.myview
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        // Initialize adapter with context, dataList, and imageFood
        val adapter = AdapterCam(requireContext(), dataList).apply {
            // Define action when item in RecyclerView is clicked
            onItemClick = { dataCam ->
                imageBitmap?.let { imageFood ->
                    handleGifClick(dataCam, imageFood)
                }
            }
        }
        recyclerView.adapter = adapter
    }

    private fun handleGifClick(dataCam: DataCam, imageFood: Bitmap) {
        // Determine the GIF resource ID based on the selected dataCam
        val gifResId = when {
            dataCam.durasiBerlari > 0 -> R.drawable.cardio
            dataCam.durasiWorkout > 0 -> R.drawable.workout
            dataCam.durasiSitUp > 0 -> R.drawable.situp
            dataCam.durasiSkipping > 0 -> R.drawable.skipping
            else -> R.drawable.skipping // Default if none match
        }

/*        // Launch ExerciseActivity with selected data
        (activity as? MainActivity)?.let { mainActivity ->
            mainActivity.showExerciseActivity(
                gifResId,
                dataCam.kalori,
                dataCam.durasiBerlari,
                dataCam.dataTitle,
                dataCam.dataDesc,
            )
        }*/
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_DATA_LIST = "dataList"
        private const val ARG_IMAGE_BITMAP = "imageBitmap"

        fun show(fragmentManager: FragmentManager, dataList: ArrayList<DataCam>, imageBitmap: Bitmap) {
            val bottomSheet = BottomSheetF().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_DATA_LIST, dataList)
                    putParcelable(ARG_IMAGE_BITMAP, imageBitmap)
                }
            }
            bottomSheet.show(fragmentManager, "DataBottomSheetFragment")
        }
    }
}
