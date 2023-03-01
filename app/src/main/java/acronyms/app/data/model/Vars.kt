package acronyms.app.data.model

import com.google.gson.annotations.SerializedName


data class Vars (

  @SerializedName("lf"    ) var lf    : String? = null,
  @SerializedName("freq"  ) var freq  : Int    =  0,
  @SerializedName("since" ) var since : Int    = 0

)