package com.example.padicareapp.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.padicareapp.data.response.ArticleResponse
import com.example.padicareapp.data.retrofit.ApiConfig
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _diseases = MutableLiveData<List<Disease>>()
    val diseases: LiveData<List<Disease>> get() = _diseases

    private val _articles = MutableLiveData<List<Article>>()
    val articles: LiveData<List<Article>> get() = _articles

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading


    fun fetchHomeData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Simulate API call with a delay
                delay(2000)

                // Load dummy diseases
                val dummyDiseases = listOf(
                    Disease(
                        "1",
                        "Brown Spot",
                        "https://upload.wikimedia.org/wikipedia/commons/9/9c/Cochliobolus_miyabeanus.jpg",
                        "Brown Spot (Bercak Coklat) adalah penyakit yang disebabkan oleh jamur Cochliobolus miyabeanus yang menyerang daun padi.",
                        "Rotasi tanaman, pemilihan varietas padi yang tahan penyakit, serta penggunaan fungisida yang tepat.",
                                "Penyemprotan fungisida berbahan aktif Mancozeb atau Propiconazole."
                    ),
                    Disease(
                        "2",
                        "Hispa",
                        "https://content.peat-cloud.com/thumbnails/rice-hispa-rice-1553250919.jpg",
                        "Hispa adalah serangan hama dari kumbang Dicladispa armigera yang menyerang daun padi dan menyebabkan daun menjadi berlubang.",
                                "Menjaga kebersihan lahan, pemusnahan sisa-sisa tanaman, dan penggunaan varietas padi yang tahan terhadap hama.",
                                "Penggunaan insektisida berbahan aktif Imidacloprid atau Cypermethrin."
                    ),
                    Disease(
                        "3",
                        "Leaf Blast (Hawar Daun)",
                        "https://www.agronasa.com/wp-content/uploads/2019/10/penyebab-penyakit-blast.jpg",
                        "Hawar Daun (Leaf Blast) disebabkan oleh jamur Pyricularia oryzae yang menginfeksi daun padi, menyebabkan bercak berwarna putih dengan pinggiran coklat.",
                                "Menjaga kelembaban tanah yang seimbang dan menghindari penanaman padi terlalu rapat.",
                                "Penyemprotan fungisida berbahan aktif Tricyclazole atau Propiconazole."
                    ),
                    Disease(
                        "4",
                        "Tungro",
                        "https://gapoktansekarsari.wordpress.com/wp-content/uploads/2016/07/4.png",
                        "Tungro adalah penyakit yang disebabkan oleh virus dan ditularkan melalui serangga penghisap seperti wereng.",
                                "Pengendalian serangga penghisap, serta penggunaan varietas padi yang tahan terhadap virus.",
                                "Tidak ada pengobatan spesifik, namun pengendalian hama penghisap dengan insektisida dapat mengurangi penyebaran."
                    ),
                    Disease(
                        "5",
                        "Busuk Batang",
                        "https://agrokomplekskita.com/wp-content/uploads/2017/04/GEJALA-PADI.jpg",
                        "Busuk Batang disebabkan oleh jamur Rhizoctonia solani yang menginfeksi pangkal batang padi, menyebabkan tanaman mati mendadak.",
                                "Penggunaan bibit yang sehat, pengaturan jarak tanam yang tepat, dan drainase yang baik.",
                                "Penggunaan fungisida seperti Benomyl atau Thiophanate methyl pada tanaman yang terinfeksi."
                    ),
                    Disease(
                        "6",
                        "Hawar Pelepah Padi",
                        "https://content.peat-cloud.com/thumbnails/rice-sheath-blight-rice-1581427208.jpg",
                        "Hawar Pelepah Padi disebabkan oleh jamur Rhizoctonia solani yang menyerang pelepah padi dan menyebabkan pembusukan.",
                                "Menjaga kebersihan lahan dan menghindari kelembaban yang berlebihan pada tanaman padi.",
                                "Penyemprotan fungisida berbahan aktif Flusilazole atau Azoxystrobin."
                    )
                )
                _diseases.value = dummyDiseases

                ApiConfig.getApiService().getArticle().enqueue(object : Callback<ArticleResponse> {
                    override fun onResponse(call: Call<ArticleResponse>, response: Response<ArticleResponse>) {
                        _isLoading.value = true
                        if (response.isSuccessful) {
                            // Map ArticlesItem to Article
                            response.body()?.articles?.let { articlesItemList ->
                                val articleList = articlesItemList.map { articleItem ->
                                    // Convert ArticlesItem to Article
                                    Article(
                                        id = articleItem?.id ?: "",
                                        title = articleItem?.title ?: "",
                                        summary = articleItem?.summary ?: "",
                                        imageUrl = articleItem?.imageUrl ?: "",
                                        content = articleItem?.content ?: "",
                                        source = articleItem?.source ?: "",
                                        publishedDate = articleItem?.publishedDate ?: ""
                                    )
                                }
                                _articles.value = articleList
                            }
                        } else {
                            // Handle unsuccessful response, maybe show an error message
                            _articles.value = emptyList()
                        }
                        _isLoading.value = false
                    }

                    override fun onFailure(call: Call<ArticleResponse>, t: Throwable) {
                        _isLoading.value = false
                        // Handle failure, maybe show a Toast or log the exception
                        _articles.value = emptyList()
                    }
                })



                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
            }
        }
    }

}
