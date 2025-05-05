import javax.swing.*; // swing 패키지를 임포트해서 스윙 컨테이너를 사용할 수 있게 하여 GUI에 사용 가능하게 함
import java.awt.*; // awt 패키지를 임포트해서 GUI구성요소를 사용 가능하게 함
import java.awt.event.*; // awt 이벤트 패키지를 임포트해서 리스너를 사용 가능하게 함

public class CalculatorGUI extends JFrame { // // CalculatorGUI 클래스를 선언하는데, 이 클래스는 Swing 컴포넌트 중 JFrame을 상속받아 GUI 환경의 프레임 역할을 함
    // 아래는 필드 선언 부분
    private JTextField display; // JFrame을 상속받아 생성된 GUI에 JTextField 컴포넌트를 선언하고 사용자가 볼 수 있도록 선언
    private double result = 0; // 계산 결과변수를 더블형으로 선언하고 0으로 초기화
    private String operator = ""; // 오퍼레이터 즉 연산자를 스트링 형으로 선언하고 ""(아무것도 없는 값)으로 초기화 
    private boolean startNewNumber = true; // 새로운 숫자를 입력할지 여부를 나타내는 플래그를 불리안(T/F형)으로 선언하고, 구체적 원리는 true면 새 입력을 시작
    private double lastOperand = 0; // 마지막 값을 저장하는 변수를 더블형으로 선언하고 0으로 초기화

    public CalculatorGUI() { // CalculatorGUI 클래스의 생성자 선언
        // 프레임 기본 골격 정하기
        setTitle("Calculator"); // 프레임의 제목을 Calculator로 정의
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 사용자가 X 버튼을 누르면 프로그램이 종료되도록 함
        setLayout(new BorderLayout()); // BorderLayout 객체를 새로 생성해서 배치관리자를 사용가능하게 함 

        // 계산기의 숫자 표시 부분의 정의, 설정
        display = new JTextField("0");  // display라는 JTextField 컴포넌트를 새로 선언하고, 그 초기값을 0으로 초기화
        display.setFont(new Font("Arial", Font.PLAIN, 40)); // 폰트는 Arial, 크기는 40으로 설정
        display.setHorizontalAlignment(SwingConstants.RIGHT); // 입력된 숫자를 오른쪽으로 정렬
        display.setEditable(false); // 계산기에 표시되는 값을 수정할 수 없도록 설정
        add(display, BorderLayout.NORTH); // add 메서드를 선언하는데 display 멤버변수,즉 JTextField가 북쪽에 위치하여 표시하도록 선언
        display.addKeyListener(new CalculatorKeyListener()); // 키보드 입력값을 JTextField에 추가하기 위해 키리스너 기능을 넣음, 입력 값에 따른 기능은 아래 내부 클래스 참조

        // 버튼부 골격 정하기
        JPanel panel = new JPanel(); // panel 이라는 객체를 생성해서 그 안에 계산기의 숫자와 연산자를 배치하기 위한 틀을 만듬
        panel.setLayout(new GridLayout(5, 4, 10, 10)); // 패널의 레이아웃을 아래로 5, 옆으로 4개의 레이아웃으로 설정하고 서로간의 간격은 10으로 설정

        // 계산기 패널에 쓸 숫자와 연산자를 배열로 정의
        String[] buttons = {
            "C", "+/-", "%", "/",
            "7", "8", "9", "*",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "0", ".", "=", "<"
        };

       // 각 버튼을 생성하고 패널에 추가하는 반복문
        for (String text : buttons) { /* buttons 배열의 요소를 하나씩 가져와서, text 변수에 넣음, 반복할 때마다 text 변수는 buttons[0], buttons[1], ... 로 설정
        	결론적으로 보았을때 아래의 내용인 버튼 객체 생성, 버튼객체의 폰트 설정, 버튼 객체의 리스너 정의, 패널에 버튼 객체를 추가하는 내용들을 배열 buttons[0]부터 해서 끝까지 반복 */
            JButton button = new JButton(text); // 배열에서 정의한 숫자와 연산자가 포함된 text 변수를 가져와 button 이라는 JButton 컴포넌트를 새로 선언 쉽게 말해 버튼 만들기
            button.setFont(new Font("Arial", Font.PLAIN, 30)); // 버튼의 폰트를 Arial로 하고 크기는 30으로 설정
            button.addActionListener(new ButtonClickListener()); // 버튼에 액션 리스너 기능을 넣어 버튼을 클릭할때 버튼 클릭 리스너의 내용을 실행하도록 함, 아래 내부 클래스 참조
            panel.add(button); // 페널에 버튼 추가
        }

        add(panel, BorderLayout.CENTER); // 버튼들이 있는 패널을 프레임의 중앙에 배치
        setSize(400, 600);// 프레임 크기를 가로 400, 세로 600으로 정의
        setVisible(true); // 프레임을 보이도록 설정
    }

    // ButtonClickListener의 내부 클래스
    private class ButtonClickListener implements ActionListener { // ButtonClickListener (리스너) 클래스는 ActionListener 인터페이스를 구현하여 이벤트를 처리
        @Override // 즉 ButtonClickListener 클래스는 인터페이스 구현을 위해 추상 메서드 actionPerformed(ActionEvent e)를 오버라이딩 함
        public void actionPerformed(ActionEvent e) {  // ActionListener 인터페이스의 추상 메서드를 구현하는 메서드, Action 이벤트가 발생하는 경우
            String command = e.getActionCommand(); // 클릭된 버튼의 텍스트를 가져와 command 변수에 저장
            handleInput(command); // command를 handleInput 메서드에 집어넣음, 아래에 handleInput 매서드 선언을 하는데 이 메서드는 계산기의 주요 연산기능을 담당, 아래 메서드 선언 참고
            display.requestFocusInWindow(); // 버튼 클릭 후에도 JTextField가 포커스를 유지해서 새로운 입력이 계속 가능하도록 함
        }
    }

    // CalculatorKeyListener의 내부 클래스
    private class CalculatorKeyListener implements KeyListener { // CalculatorKeyListener 클래스는 KeyListener 인터페이스를 구현하여 키보드 입력 이벤트를 처리
        @Override //즉 CalculatorKeyListener 클래스는 인터페이스 구현을 위해 추상 메서드 keyTyped(KeyEvent e)를 오버라이딩 함
        public void keyTyped(KeyEvent e) { // KeyListener 인터페이스의 추상 메서드를 구현하는 메서드, 키 이벤트가 발생하는 경우 즉 키가 타이핑되었을 때 작동
            char keyChar = e.getKeyChar(); // 사용자가 입력한 키의 문자 값을 char 형으로 keyChar 변수에 저장
            if (Character.isDigit(keyChar) || keyChar == '.' || "+-*/=%".indexOf(keyChar) != -1) { 
            	/* 입력값이 숫자일 경우, 소숫점 일 경우 트루를 반환, 입력된 값을 인덱스 오브를 찍어서 -1이 아닌경우 트루를 반환, 
            	 * 인덱스 오브 값은 +는 0, -는 1, *는 2, /는 3, %는 4, =은 5임
            	 * 즉 연산자가 입력된 것을 판별, 이걸 모두 || 즉 or연산자로 묶어 숫자, 소숫점, 연산자가 입력 될 경우에 대한 조건문
            	 */
                handleInput(String.valueOf(keyChar)); // 입력값을 그대로 처리
            } else if (keyChar == KeyEvent.VK_ENTER) { // 그 외에 엔터를 치면
                handleInput("="); // 엔터는 = 버튼을 누른 것으로 처리
            } else if (keyChar == KeyEvent.VK_BACK_SPACE) { // 백스페이스를 치면 
                handleInput("<"); // < 버튼을 누른 것으로 처리
            } else { // 그 외 다른 키를 눌렀을 때 
                e.consume(); // 이벤트를 소비시켜서 입력을 무효화 시킴
            }
        }

        @Override //즉 CalculatorKeyListener 클래스는 인터페이스 구현을 위해 추상 메서드 keyPressed(KeyEvent e)를 오버라이딩 함
        public void keyPressed(KeyEvent e) { } // 키가 눌릴 때 호출, 현재 구현에서는 필요 없음
        
        @Override //즉 CalculatorKeyListener 클래스는 인터페이스 구현을 위해 추상 메서드 keyReleased(KeyEvent e)를 오버라이딩 함
        public void keyReleased(KeyEvent e) { } // 키가 떼어질 때 호출, 현재 구현에서는 필요 없음
    }

    // 계산기 기능 처리를 위한 메서드
    private void handleInput(String command) { // handleInput 메서드에 입력 값이 String 형으로 입력
        if (command.matches("[0-9.]")) { // 숫자나 소수점이 눌린 경우
            if (startNewNumber) { // 새로운 숫자를 눌러야 하는 경우, 연산자 버튼이 눌렸거나 처음 입력할 때
                display.setText(command); // 입력 된 값이 command로 설정되고 그 값이 JTextField에 반영, 기존에 표시되던 값은 삭제
                startNewNumber = false;// 입력되는 값이 더 이상 새로운 값이 아니라는 것을 설정, 플래그? 토큰? 같은 성격
            } else { // 기존에 입력된 값에 추가로 입력할 때
                display.setText(display.getText() + command); // 기존 입력값에 새로운 값을 이어서 JTextField에 반영
            }
        } else if (command.equals("=")) { // = 버튼이 눌린 경우
            if (!operator.isEmpty()) { // 연산자가 비어있지 않을경우, 즉 연산자가 입력되어 있을 때
                if (startNewNumber) { // = 버튼이 연속으로 눌린 경우
                    calculate(lastOperand); // lastOperand에 있는 값을 이용해서 연산을 다시 수행 그러니까 = 버튼이 연속적으로 눌릴때 반복연산 함
                } else { // 그냥 = 이 눌린 경우
                    lastOperand = Double.parseDouble(display.getText()); // 현재 입력된 값을 lastOperand에 저장함
                    calculate(lastOperand); // 현재 입력된 값을 사용하여 계산 수행
                }
                startNewNumber = true; // 다음 입력은 새로운 숫자로 시작하도록 설정함
            }
        } else if (command.equals("C")) { // C 버튼이 눌린 경우 모든 값을 초기화
            result = 0; // 결과값을 0으로 초기화
            operator = ""; // 연산자 초기화
            display.setText("0"); // 디스플레이에 표시되는 값 즉 JTextField에 0으로 초기화
            startNewNumber = true; // 추후 입력되는 값이 새로운 값 이라고 설정
        } else if (command.equals("<")) { // < 버튼이 눌린 경우, 백스패이스가 눌린 경우 하나씩 삭제
            String currentText = display.getText();// String형 으로 currentText(현재 텍스트)를 선언 하는데, 현재 JTextField에 들어있는 텍스트를 가져와서 저장
            if (currentText.length() > 1) { // 현재 텍스트가 길이가 1보다 길 때 
                display.setText(currentText.substring(0, currentText.length() - 1)); // 마지막 문자 한개씩 제거하여 JTextField의 값에 반영
            } else { // 텍스트 길이가 1일때 즉 한자리 수일 때
                display.setText("0"); // 디스플레이에 표시되는 값 즉 JTextField에 0으로 초기화
                startNewNumber = true; // 추후 입력되는 값이 새로운 값 이라고 설정
            }
        } else if (command.equals("+/-")) { // +/- 버튼이 눌린 경우
            double currentValue = Double.parseDouble(display.getText()); // double 형으로 변수 currentValue를 선언하고, 현재 JTextField에 들어있는 텍스트를 double 형으로 파싱해서 저장
            currentValue *= -1; // currentValue 즉 현재값에 -1을 곱해서 부호를 반전
            display.setText(formatResult(currentValue)); // 바뀐 값을 String 형으로 변환하여 JTextField에 반영
        } else if (command.equals("%")) { // '%' 버튼이 눌린 경우 
            double currentValue = Double.parseDouble(display.getText()); // double 형으로 변수 currentValue를 선언하고, 현재 JTextField에 들어있는 텍스트를 double 형으로 파싱해서 저장
            currentValue /= 100; // currentValue 즉 현재값에 나누기 100을 해서 백분율로 변환
            display.setText(formatResult(currentValue)); // 바뀐 값을 String형으로 변환하여 JTextField에 반영
        } else { // 그 외 +, -, *, / 이 눌린 경우
            if (!operator.isEmpty() && !startNewNumber) { 
            	/* 연산자가 선택된 경우와 새로 시작하는 경우가 아닐때 예를 들어 5 + 5를 입력한 상황에 다른 연산자를 입력할때 
            	* 즉 풀어서 보면 선택되어 있을때 false인데 !때문에 true로 바뀌고, 처음 입력할때가 아닐때 false인데 !때문에 true로 바뀌고 AND로 둘다 트루일 경우에 작동 
            	*/
                calculate(Double.parseDouble(display.getText())); // 현재 JTextField에 들어있는 텍스트를 double 형으로 파싱 후 calculate 메서드를 호출해서 계산
                // 이 경우는 5 + 5를 누르고 한번 더 다른 연산자를 누르면 이전 연산을 완료하도록 함 즉 5+5 다음 -를 누르면 10 - 가 됨 
            } else { // 새로운 값을 입력하고 연산자를 입력하는 경우
                result = Double.parseDouble(display.getText()); // 결과값을 현재 입력된 값으로 정의
            }
            operator = command; // 새로운 연산자를 설정
            display.setText(formatResult(result) + " " + operator); // JTextField에 "결과값 + 띄우고 + 연산자"로 설정
            startNewNumber = true; //  추후 입력되는 값이 새로운 값 이라고 설정
        }
    }

    private void calculate(double x) { // 연산에 사용할 숫자 x를 double 형식으로 받아 계산 하는 메서드
        switch (operator) { // 입력된 연산자가 뭐냐에 따라 작동
            case "+": // 입력값이 +면
                result += x; // x 값에 덧셈 후 결과 값에 저장
                break; // 벗어남
            case "-": // 입력값이 -면
                result -= x; // x 값에 뺄셈 후 결과 값에 저장
                break; // 벗어남
            case "*": // 입력값이 *면
                result *= x; // x 값에 곱셈 후 결과 값에 저장
                break; // 벗어남
            case "/": // 입력값이 /면
                if (x != 0) { // 나누는 값이 0이 아닐때
                    result /= x; // 나누기를 그대로 진행 후 결과값에 저장
                } else { // 0으로 나누려고 할 때
                    JOptionPane.showMessageDialog(null, "0으로 나눌 수 없습니다", "Error", JOptionPane.ERROR_MESSAGE); // 0으로 나눌 수 없다고 알림
                    return; // 나누기를 취소하고 메서드를 종료
                }
                break; // 벗어남
        }
        display.setText(formatResult(result)); // 연산 결과를 JTextField에 반영
    }

    // 결과값의 형식이 정수일때 형식 설정 메소드 예를 들어 5일때 5.0이 아닌 5로 뜨게끔 
    private String formatResult(double value) { // 더블형의 값을 문자열로 변환하여 결과 값을 반영하는 메서드 선언 
        if (value == (long) value) { // 값이 정수일때
            return String.valueOf((long) value); // 정수로 변환 후 반환
        } else { // 정수가 아닐때
            return String.valueOf(value); // 실수를 그대로 반환
        }
    }

    public static void main(String[] args) { // 프로그램의 실행 시작 지점을 나타내는 메인 메소드, 프로그램의 시작 부분
        new CalculatorGUI(); // CalculatorGUI 객체를 생성하고 이를 화면에 나타냄 (setVisible(true))
    }
}