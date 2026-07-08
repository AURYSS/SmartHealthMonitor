package mx.utng.carh.tv

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import mx.utng.carh.smarthealthmonitor.data.db.LecturaFC

class MainFragment : BrowseSupportFragment() {
    private val viewModel: TvViewModel by viewModels()
    private lateinit var rowsAdapter: ArrayObjectAdapter
    private lateinit var estadoAdapter: ArrayObjectAdapter
    private lateinit var histAdapter: ArrayObjectAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Configuración del BrowseFragment
        title = "SmartHealth TV"
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true
        
        // Color de la marca en el sidebar
        brandColor = resources.getColor(R.color.sh_primary, null)

        cargarFilas()
        observarDatos()
    }

    private fun observarDatos() {
        // Observar FC actual (Estado actual)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.fc.collect { valor ->
                    estadoAdapter.clear()
                    // Usamos el modelo LecturaFC para mostrar el valor actual
                    estadoAdapter.add(LecturaFC(valorBpm = valor, hora = "Ahora"))
                }
            }
        }

        // Observar historial de Room y actualizar la fila
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.historial.collect { lecturas ->
                    histAdapter.clear()
                    lecturas.forEach { histAdapter.add(it) }
                }
            }
        }
    }

    private fun cargarFilas() {
        rowsAdapter = ArrayObjectAdapter(ListRowPresenter())

        // Fila 1: Estado actual
        estadoAdapter = ArrayObjectAdapter(FCCardPresenter())
        rowsAdapter.add(ListRow(HeaderItem(0, "Estado actual"), estadoAdapter))

        // Fila 2: Historial con adapter reactivo
        histAdapter = ArrayObjectAdapter(FCCardPresenter())
        rowsAdapter.add(ListRow(HeaderItem(1, "Historial FC"), histAdapter))

        this.adapter = rowsAdapter
    }
}
