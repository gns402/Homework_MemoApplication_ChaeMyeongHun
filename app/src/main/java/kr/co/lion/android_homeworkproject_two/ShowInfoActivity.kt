package kr.co.lion.android_homeworkproject_two

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import kr.co.lion.android_homeworkproject_two.databinding.ActivityShowInfoBinding

class ShowInfoActivity : AppCompatActivity() {

    lateinit var activityShowInfoBinding: ActivityShowInfoBinding

    // ModifyActivity의 런처
    lateinit var modifyActivityLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityShowInfoBinding = ActivityShowInfoBinding.inflate(layoutInflater)
        setContentView(activityShowInfoBinding.root)

        initData()
        setToolbar()
        setView()
    }


    // 기본 데이터 및 객체 셋팅
    fun initData(){
        val contract3 = ActivityResultContracts.StartActivityForResult()
        modifyActivityLauncher = registerForActivityResult(contract3){

        }

    }

    // 툴바 구성
    fun setToolbar(){
        activityShowInfoBinding.apply {
            toolbarShowInfo.apply {
                title = "메모 보기"

                // 메뉴 등록
                inflateMenu(R.menu.menu_show)
                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationOnClickListener {
                    setResult(RESULT_CANCELED)
                    finish()
                }
                // 메뉴의 리스너
                setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menu_show_modify ->{
                            val ModifyIntent = Intent(this@ShowInfoActivity, ModifyActivity::class.java)

                            modifyActivityLauncher.launch(ModifyIntent)
                        }
                        R.id.menu_show_item_delete -> {

                        }
                    }
                    true
                }
            }
        }
    }

    // View 설정
    fun setView(){
        activityShowInfoBinding.apply {
                // Intent로 부터 메모 정보 객체를 추출한다
                val memoData = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                    intent.getParcelableExtra("memoData", MemoData::class.java)
                }else{
                    intent.getParcelableExtra("memoData")
                }
            textFieldInputShowInfoTitle.setText("${memoData?.memoTitle}")
            textFieldInputShowInfoDate.setText("${memoData?.memoDate}")
            textFieldInputShowInfoDetail.setText("${memoData?.memoDetail}")
        }
    }

}