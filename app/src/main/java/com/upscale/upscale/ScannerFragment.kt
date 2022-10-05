package com.upscale.upscale

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.upscale.upscale.analyzer.BarcodeAnalyzer
import com.upscale.upscale.app.UpscaleApplication
import com.upscale.upscale.databinding.FragmentScannerBinding
import com.upscale.upscale.viewmodels.FindFoodViewModel
import com.upscale.upscale.viewmodels.FindFoodViewModelFactory
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean


class ScannerFragment : Fragment() {

    private val TAG = "Scanner Fragment"

    private var _binding: FragmentScannerBinding? = null
    private val binding get() = _binding!! // Only valid between onCreateView and onDestroyView

    private var currentlyProcessingBarcode = AtomicBoolean(false)

    private val viewModel: FindFoodViewModel by navGraphViewModels(R.id.addFoodGraph){
        FindFoodViewModelFactory((requireActivity().application as UpscaleApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScannerBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentlyProcessingBarcode.set(false)
        bindCamera()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bindCamera(){

        // Set up camera preview
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener(
            {
                val cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder().build()
                    .apply { setSurfaceProvider(binding.cameraView.surfaceProvider) }

                // Configure ML Barcode Scanning

                val analysis = ImageAnalysis.Builder().build()
                    .also {
                        it.setAnalyzer(Executors.newSingleThreadExecutor(), BarcodeAnalyzer { barcode ->
                            if (currentlyProcessingBarcode.compareAndSet(false, true)) {
                                binding.barcodeEditText.setText(barcode)
                                binding.progressIndicator.visibility = View.VISIBLE
                                viewModel.searchBarcode(barcode)
                                viewModel.food.observe(viewLifecycleOwner){
                                    findNavController().navigate(R.id.action_scannerFragment_to_foodInfoFragment)
                                }

                            }
                        })
                    }

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        viewLifecycleOwner,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview,
                        analysis
                    )
                } catch (e: Exception) {
                    // If the use case has already been bound to another lifecycle or method is not called on main thread.
                    Log.e(TAG, e.message.orEmpty())
                }
            },
            ContextCompat.getMainExecutor(requireContext())
        )






    }

}