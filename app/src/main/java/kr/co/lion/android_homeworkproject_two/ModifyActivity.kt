package kr.co.lion.android_homeworkproject_two

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kr.co.lion.android_homeworkproject_two.databinding.ActivityModifyBinding
import kotlin.concurrent.thread

class ModifyActivity : AppCompatActivity() {

    lateinit var activityModifyBinding: ActivityModifyBinding

    lateinit var showInfoActivityLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityModifyBinding = ActivityModifyBinding.inflate(layoutInflater)
        setContentView(activityModifyBinding.root)

        initData()
        setToolbar()
        setView()

    }


    // 기본 데이터 및 객체 셋팅
    fun initData(){
        val contract1 = ActivityResultContracts.StartActivityForResult()
        showInfoActivityLauncher = registerForActivityResult(contract1){


        }
    }


    // 툴바 구성
    fun setToolbar(){
        activityModifyBinding.apply {
            toolbarModify.apply {
                title = "메모 수정"
                // 뒤로가기
                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationOnClickListener {
                    setResult(RESULT_CANCELED)
                    finish()
                }

                // 메뉴 등록
                inflateMenu(R.menu.menu_modify)
                setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menu_modify_done -> {
                            processInputDone()
                        }
                    }
                    true
                }
            }
        }
    }

    // View 설정
    fun setView(){
        activityModifyBinding.apply {
            // Intent로 부터 메모 정보 객체를 추출한다
            val memoData = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                intent.getParcelableExtra("memoData", MemoData::class.java)
            }else{
                intent.getParcelableExtra("memoData")
            }
            textFieldInputTitle.setText("${memoData?.memoTitle}")
            textFieldInputDetail.setText("${memoData?.memoDetail}")
        }
    }


    // 입력 완료 처리
    fun processInputDone() {
        activityModifyBinding.apply {
            // 사용자가 입력한 내용을 가져온다.
            val memoTitle = textFieldInputTitle.text.toString()
            val memoDetail = textFieldInputDetail.text.toString()

            // 입력 값에 대한 검사
            if(memoTitle.isEmpty()){
                showDialog("제목 입력 오류", "제목을 입력해주세요", textFieldInputTitle)
                return
            }
            if(memoDetail.isEmpty()){
                showDialog("내용 입력 오류", "내용을 입력해주세요", textFieldInputDetail)
                return
            }

            // 입력받은 정보를 객체에 담아 준다.
            val memoData = MemoData(memoTitle, memoDetail)

            Snackbar.make(activityModifyBinding.root, "수정이 완료되었습니다", Snackbar.LENGTH_LONG).show()


            val resultIntent = Intent()
            resultIntent.putExtra("memoData", memoData)

            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }



    // 다이얼로그 메서드
    fun showDialog(title:String, message:String, focusView: TextInputEditText){
        val builder = MaterialAlertDialogBuilder(this@ModifyActivity).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                // 입력 내용 지우기
                focusView.setText("")
                // 포커스 주기
                focusView.requestFocus()
                // 키보드를 올린다.
                showSoftInput(focusView)
            }
        }
        builder.show()
    }

    fun showSoftInput(focusView: TextInputEditText){
        thread {
            SystemClock.sleep(500)
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(focusView, 0)
        }
    }

}