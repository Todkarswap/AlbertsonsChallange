package acronyms.app.data.model

import com.google.gson.annotations.SerializedName


data class AbbrevationResult (

  @SerializedName("sf") var sf  : String?        = null,
  @SerializedName("lfs" ) var lfs : ArrayList<Lfs> = arrayListOf()

)