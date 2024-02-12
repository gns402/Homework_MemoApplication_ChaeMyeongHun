package kr.co.lion.android_homeworkproject_two

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.inputmethod.InputMethodManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kr.co.lion.android_homeworkproject_two.databinding.ActivityInputBinding
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import kotlin.concurrent.thread

class InputActivity : AppCompatActivity() {

    lateinit var activityInputBinding: ActivityInputBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityInputBinding = ActivityInputBinding.inflate(layoutInflater)
        setContentView(activityInputBinding.root)

        setToolbar()
        setView()

    }

    // 툴바 구성
    fun setToolbar(){
        activityInputBinding.apply {
            toolbarInput.apply {
                title = "메모 작성"
                // Back 할때
                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationOnClickListener {
                    setResult(RESULT_CANCELED)
                    finish()
                }

                // 메뉴 등록
                inflateMenu(R.menu.menu_input)
                setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menu_input_done -> {
                            processInputDone()
                        }
                    }
                    true
                }
            }
        }
    }

    // 뷰 설정
    fun setView(){
        activityInputBinding.apply {
            // 뷰에 포커스를 준다.
            textFieldInputTitle.requestFocus()

            // 키보드를 올린다.
            showSoftInput(textFieldInputTitle)

            // 엔터키를 누르면 입력 완료처리를 한다.
            textFieldInputDetail.setOnEditorActionListener { v, actionId, event ->
                processInputDone()
                true
            }
        }
    }


    // 입력 완료 처리
    fun processInputDone() {
        //Toast.makeText(this@InputActivity, "눌러졌습니다", Toast.LENGTH_SHORT).show()

        activityInputBinding.apply {
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

            Snackbar.make(activityInputBinding.root, "등록이 완료되었습니다", Snackbar.LENGTH_LONG).show()

            // 이전으로 돌아가기.
            // RESULT_OK일때 Intent 안에 있는 객체를 추출(studentData이거.) 해서 리스트에 담고 리사이클러뷰를 갱신한다.
            val resultIntent = Intent()
            resultIntent.putExtra("memoData", memoData)

            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }


    // 다이얼로그 메서드
    fun showDialog(title:String, message:String, focusView:TextInputEditText){
        val builder = MaterialAlertDialogBuilder(this@InputActivity).apply {
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