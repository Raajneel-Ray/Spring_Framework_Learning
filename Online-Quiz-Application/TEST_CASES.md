# Online Quiz Application – Test Cases

Base URL: `http://localhost:8080`  
Application is in-memory (data resets on restart).

---

## Section A – UI Test Cases (Manual Browser Testing)

### A.1 Registration

| TC# | Steps | Expected Result |
|-----|-------|----------------|
| UI-R-01 | Open `http://localhost:8080/register` | Registration form loads with fields: Username, Password, Confirm Password, Email, Role |
| UI-R-02 | Submit empty form | All required fields highlighted in red; no navigation |
| UI-R-03 | Enter username `ab` (< 3 chars) and submit | Username field shows validation error |
| UI-R-04 | Enter password `abc` (< 6 chars) and submit | Password field shows validation error |
| UI-R-05 | Enter password `pass123`, confirm `wrong456` and submit | Confirm Password shows "Passwords do not match" |
| UI-R-06 | Enter invalid email `notanemail` | Email field shows validation error |
| UI-R-07 | Leave Role as default (unselected) | Role field shows "Please select a role" |
| UI-R-08 | Fill all fields correctly (User role): username=`john`, password=`pass123`, confirm=`pass123`, email=`john@test.com`, role=`USER` | Redirected to `/login?success` with green success banner |
| UI-R-09 | Fill all fields correctly (Admin role): username=`admin1`, password=`admin123`, email=`admin1@test.com`, role=`ADMIN` | Redirected to `/login?success` |
| UI-R-10 | Try to register again with `john` (duplicate username) | Redirected to `/register?error` with red error banner |
| UI-R-11 | Click "Login here" link | Navigates to `/login` |

---

### A.2 Login

| TC# | Steps | Expected Result |
|-----|-------|----------------|
| UI-L-01 | Open `http://localhost:8080/login` | Login form loads with Username, Password fields and "Register here" link |
| UI-L-02 | Submit empty form | Required fields highlighted; no navigation |
| UI-L-03 | Enter username only, submit | Password field shows validation error |
| UI-L-04 | Enter wrong credentials (`bad`/`user`) | Redirected to `/login?error` with red error banner |
| UI-L-05 | Login as `john` / `pass123` (USER) | Redirected to `/home` which renders the Quiz page (list of questions) |
| UI-L-06 | Login as `admin1` / `admin123` (ADMIN) | Redirected to `/home` which renders the **QuizList** (admin view) directly |
| UI-L-07 | Click the eye icon on the password field | Password text toggles between visible/hidden |
| UI-L-08 | Click "Register here" | Navigates to `/register` |
| UI-L-09 | Access `http://localhost:8080/home` without logging in | Redirected to `/login` |

---

### A.3 Admin – Quiz Management (`/QuizList`)

> Login as ADMIN. `/home` automatically renders the QuizList (admin view).

| TC# | Steps | Expected Result |
|-----|-------|----------------|
| UI-Q-01 | Open `/QuizList` as ADMIN (no questions added yet) | Empty-state card shown with "Add Question" button |
| UI-Q-02 | Click "Add New Question" | Navigates to `/addQuiz` |
| UI-Q-03 | After adding questions (see A.4), open `/QuizList` | Table showing question text, options, correct answer, Edit/Delete buttons |
| UI-Q-04 | Click Edit button on a question | Navigates to `/editQuiz/{id}` with form pre-filled |
| UI-Q-05 | Click Delete button on a question | Confirmation modal appears |
| UI-Q-06 | Confirm delete in modal | Question removed; returns to `/home` which shows the updated QuizList |
| UI-Q-07 | Try to open `/QuizList` as USER | 403 Forbidden |

---

### A.4 Admin – Add Quiz Question (`/addQuiz`)

| TC# | Steps | Expected Result |
|-----|-------|----------------|
| UI-A-01 | Open `/addQuiz` as ADMIN | Empty form with: Question Text, 4 Option inputs (A–D), Correct Answer dropdown |
| UI-A-02 | Submit empty form | All required fields highlighted |
| UI-A-03 | Fill Question Text only (leave options empty) | Option fields show validation errors |
| UI-A-04 | Fill Question Text + Options A, B, C, D | Correct Answer dropdown auto-populates with the 4 option values |
| UI-A-05 | Add a complete question: `What is 2+2?`, options `One,Two,Three,Four`, correct=`Four` | Redirected to `/home` (QuizList); new question is visible in the table |
| UI-A-06 | Click Cancel | Returns to `/home` |
| UI-A-07 | Try to open `/addQuiz` as USER | 403 Forbidden |

---

### A.5 Admin – Edit Quiz Question (`/editQuiz/{id}`)

| TC# | Steps | Expected Result |
|-----|-------|----------------|
| UI-E-01 | Click Edit on a question in QuizList | Form opens pre-populated with existing question text, options, and correct answer |
| UI-E-02 | Clear Question Text and submit | Question text validation error shown |
| UI-E-03 | Modify question text and submit | Returns to `/home` (QuizList); updated question text is visible in the table |
| UI-E-04 | Change an option value; verify Correct Answer dropdown updates | Dropdown reflects new option text |
| UI-E-05 | Change Correct Answer selection and submit | Returns to `/home` (QuizList); updated correct answer is visible in the table |
| UI-E-06 | Click Cancel | Returns to `/home` without changes |

---

### A.6 User – Take Quiz (`/Quiz` or `/home`)

> Login as USER. `/home` renders the Quiz page.

| TC# | Steps | Expected Result |
|-----|-------|----------------|
| UI-Z-01 | Login as USER | Quiz page rendered at `/home` with all questions and radio button options |
| UI-Z-02 | No questions in the system | Empty-state message "No Questions Available" |
| UI-Z-03 | Select an answer – verify option label highlights | Selected option label becomes bold |
| UI-Z-04 | Click Submit without answering all questions | Red warnings appear under unanswered questions; page scrolls to first unanswered |
| UI-Z-05 | Answer all questions correctly and submit | Redirected to `/result` page showing score |
| UI-Z-06 | Answer all questions wrongly and submit | Redirected to `/result` page showing 0 score |
| UI-Z-07 | Try to access `/QuizList` as USER | 403 Forbidden |

---

### A.7 Result Page

| TC# | Steps | Expected Result |
|-----|-------|----------------|
| UI-RES-01 | Submit quiz with all correct answers | Trophy icon, "Well Done!", 100% progress bar (green) |
| UI-RES-02 | Submit quiz with 0 correct answers | Neutral icon, "Keep Practising!", red progress bar |
| UI-RES-03 | Score between 40%–69% | Smile icon, "Good Effort!", blue progress bar |
| UI-RES-04 | Click "Retake Quiz" | Returns to `/home` (Quiz page) |
| UI-RES-05 | Click Logout | Redirected to `/login?logout` with logout message |
| UI-RES-06 | Verify progress bar animates on load | Bar expands from 0% to actual score over 1 second |

---

### A.8 Logout

| TC# | Steps | Expected Result |
|-----|-------|----------------|
| UI-OUT-01 | Click Logout from any page | Redirected to `/login?logout`; blue "You have been logged out" banner shows |
| UI-OUT-02 | After logout, click browser Back button to a protected page | Redirected to `/login` (session invalidated) |

---

## Section B – Postman API Test Cases

### Setup Instructions

Spring Security uses **session-based CSRF protection**. Every POST request
requires a CSRF token. Follow these steps in Postman:

**One-time Setup (Postman Collection Variables):**
```
baseUrl  = http://localhost:8080
```

**Session Workflow:**
1. `GET /login` — captures the `JSESSIONID` cookie and the `_csrf` token from HTML
2. `POST /login` — authenticates and gets an upgraded session
3. All subsequent requests reuse the `JSESSIONID` cookie (enable "Automatically follow redirects" = OFF for step 2)

**Pre-request Script to extract CSRF from HTML** (add to the GET /login request):
```javascript
// After GET /login, the HTML contains:
// <input type="hidden" name="_csrf" value="TOKEN">
const html = pm.response.text();
const match = html.match(/name="_csrf"\s+value="([^"]+)"/);
if (match) {
    pm.collectionVariables.set("csrfToken", match[1]);
}
```

---

### B.1 Registration Flow

#### TC-POST-REG-01 – Register new USER
```
Method : POST
URL    : {{baseUrl}}/register
Headers: Content-Type: application/x-www-form-urlencoded
Body   : username=testuser&password=password123&email=test@example.com&role=USER
```
**Expected:** `302` redirect to `/login?success`

---

#### TC-POST-REG-02 – Register new ADMIN
```
Method : POST
URL    : {{baseUrl}}/register
Body   : username=adminuser&password=admin123&email=admin@example.com&role=ADMIN
```
**Expected:** `302` redirect to `/login?success`

---

#### TC-POST-REG-03 – Duplicate username
```
Method : POST
URL    : {{baseUrl}}/register
Body   : username=testuser&password=newpass&email=other@example.com&role=USER
```
**Expected:** `302` redirect to `/register?error`

---

#### TC-POST-REG-04 – Invalid email
```
Method : POST
URL    : {{baseUrl}}/register
Body   : username=newuser2&password=pass123&email=notanemail&role=USER
```
**Expected:** `302` redirect to `/register?error`

---

#### TC-POST-REG-05 – Missing role
```
Method : POST
URL    : {{baseUrl}}/register
Body   : username=newuser3&password=pass123&email=new@example.com
```
**Expected:** `400 Bad Request` (missing required parameter)

---

### B.2 Authentication Flow

#### Step 1 – Get CSRF Token

```
Method : GET
URL    : {{baseUrl}}/login
```
Run the Pre-request Script above. `{{csrfToken}}` is now populated.

---

#### TC-POST-LOGIN-01 – Valid USER login
```
Method : POST
URL    : {{baseUrl}}/login
Headers: Content-Type: application/x-www-form-urlencoded
Body   : username=testuser&password=password123&_csrf={{csrfToken}}
```
**Expected:** `302` redirect to `/home`  
**Action:** Save the `JSESSIONID` cookie (Postman does this automatically).

---

#### TC-POST-LOGIN-02 – Valid ADMIN login
```
Method : POST
URL    : {{baseUrl}}/login
Body   : username=adminuser&password=admin123&_csrf={{csrfToken}}
```
**Expected:** `302` redirect to `/home`

---

#### TC-POST-LOGIN-03 – Wrong password
```
Method : POST
URL    : {{baseUrl}}/login
Body   : username=testuser&password=wrongpassword&_csrf={{csrfToken}}
```
**Expected:** `302` redirect to `/login?error`

---

#### TC-POST-LOGIN-04 – Non-existent user
```
Method : POST
URL    : {{baseUrl}}/login
Body   : username=nobody&password=pass123&_csrf={{csrfToken}}
```
**Expected:** `302` redirect to `/login?error`

---

### B.3 Admin – Quiz CRUD (Authenticate as ADMIN first)

#### TC-GET-HOME-01 – Home page (authenticated)
```
Method : GET
URL    : {{baseUrl}}/home
```
**Expected:** `200 OK`, HTML body of QuizList.html (admin view) for ADMIN session; Quiz.html for USER session

---

#### TC-GET-QUIZLIST-01 – Admin quiz list
```
Method : GET
URL    : {{baseUrl}}/QuizList
```
**Expected:** `200 OK`, HTML showing the admin quiz management table  
**If USER session:** `403 Forbidden`

---

#### TC-GET-ADDQUIZ-01 – Add quiz form
```
Method : GET
URL    : {{baseUrl}}/addQuiz
```
**Expected:** `200 OK`, HTML of the addQuiz form  
**If USER session:** `403 Forbidden`

---

#### TC-POST-ADDQUIZ-01 – Add a valid question (ADMIN session)

> First refresh CSRF: GET `/addQuiz` and re-run the CSRF extraction script targeting the form hidden input.

```
Method : POST
URL    : {{baseUrl}}/addQuiz
Headers: Content-Type: application/x-www-form-urlencoded
Body   :
  questionText=What is the capital of France?
  &optionsFromString=Paris,London,Berlin,Madrid
  &correctAnswer=Paris
  &_csrf={{csrfToken}}
```
**Expected:** `302` redirect to `/home`  
**Verify:** GET `/QuizList` should now show the question.

---

#### TC-POST-ADDQUIZ-02 – Add question as USER (should fail at security layer)
```
(Switch to USER session first)
Method : POST
URL    : {{baseUrl}}/addQuiz
Body   : questionText=Test?&optionsFromString=A,B,C,D&correctAnswer=A&_csrf={{csrfToken}}
```
**Expected:** `403 Forbidden`

---

#### TC-POST-ADDQUIZ-03 – Add second question
```
(ADMIN session)
Method : POST
URL    : {{baseUrl}}/addQuiz
Body   :
  questionText=What is 5 × 5?
  &optionsFromString=10,20,25,30
  &correctAnswer=25
  &_csrf={{csrfToken}}
```
**Expected:** `302` redirect to `/home`

---

#### TC-GET-EDITQUIZ-01 – Edit quiz form (note the ID from QuizList)
```
Method : GET
URL    : {{baseUrl}}/editQuiz/1
```
**Expected:** `200 OK`, form pre-populated with question ID 1 data  
**If question doesn't exist:** NullPointerException / 500 (no error handling in backend)

---

#### TC-POST-EDITQUIZ-01 – Submit edit
```
Method : POST
URL    : {{baseUrl}}/editQuestion
Body   :
  id=1
  &questionText=What is the capital of Germany?
  &optionsFromString=Paris,London,Berlin,Rome
  &correctAnswer=Berlin
  &_csrf={{csrfToken}}
```
**Expected:** `302` redirect to `/home`  
**Verify:** GET `/QuizList` shows updated question.

---

#### TC-GET-DELETE-01 – Delete a question
```
Method : GET
URL    : {{baseUrl}}/deleteQuestion/1
```
**Expected:** `302` redirect to `/home`  
**Verify:** GET `/QuizList` no longer shows that question.

---

#### TC-GET-DELETE-02 – Delete as USER (should fail)
```
(USER session)
Method : GET
URL    : {{baseUrl}}/deleteQuestion/1
```
**Expected:** `403 Forbidden`

---

### B.4 User – Take Quiz and Submit (Authenticate as USER first)

#### TC-GET-QUIZ-01 – Quiz page
```
Method : GET
URL    : {{baseUrl}}/home
```
**Expected:** `200 OK`, Quiz.html with radio button questions

---

#### TC-POST-SUBMIT-01 – Submit all correct answers
Assuming two questions are loaded (IDs 1 and 2). The order in answers maps to the 
index in `questionService.getQuestionsList()`.

```
Method : POST
URL    : {{baseUrl}}/submitQuiz
Headers: Content-Type: application/x-www-form-urlencoded
Body   : answer0=Paris&answer1=25&_csrf={{csrfToken}}
```
**Expected:** `200 OK`, result.html showing `score=2`, `totalQuestions=2`

---

#### TC-POST-SUBMIT-02 – Submit all wrong answers
```
Method : POST
URL    : {{baseUrl}}/submitQuiz
Body   : answer0=London&answer1=10&_csrf={{csrfToken}}
```
**Expected:** `200 OK`, result.html showing `score=0`, `totalQuestions=2`

---

#### TC-POST-SUBMIT-03 – Submit with some unanswered (null answer)
```
Method : POST
URL    : {{baseUrl}}/submitQuiz
Body   : answer0=Paris&_csrf={{csrfToken}}
```
**Expected:** `200 OK`, result.html. The unanswered question scores 0 (null check in service).

---

#### TC-POST-SUBMIT-04 – Submit as ADMIN (should fail at security layer)
```
(ADMIN session)
Method : POST
URL    : {{baseUrl}}/submitQuiz
Body   : answer0=Paris&_csrf={{csrfToken}}
```
**Expected:** `403 Forbidden`

---

### B.5 Logout

#### TC-POST-LOGOUT-01 – Logout
```
Method : POST
URL    : {{baseUrl}}/logout
Body   : _csrf={{csrfToken}}
```
**Expected:** `302` redirect to `/login?logout`

---

#### TC-GET-PROTECTED-AFTER-LOGOUT – Access after logout
```
Method : GET
URL    : {{baseUrl}}/home
(after logging out — session is invalidated)
```
**Expected:** `302` redirect to `/login`

---

### B.6 Unauthorized Access Tests

| TC# | Method | URL | Auth | Expected |
|-----|--------|-----|------|---------|
| SEC-01 | GET | `/QuizList` | No session | `302` → `/login` |
| SEC-02 | GET | `/QuizList` | USER session | `403 Forbidden` |
| SEC-03 | GET | `/addQuiz` | USER session | `403 Forbidden` |
| SEC-04 | POST | `/addQuiz` | USER session | `403 Forbidden` |
| SEC-05 | GET | `/editQuiz/1` | USER session | `403 Forbidden` |
| SEC-06 | POST | `/editQuestion` | USER session | `403 Forbidden` |
| SEC-07 | GET | `/deleteQuestion/1` | USER session | `403 Forbidden` |
| SEC-08 | GET | `/Quiz` | ADMIN session | `403 Forbidden` |
| SEC-09 | POST | `/submitQuiz` | ADMIN session | `403 Forbidden` |
| SEC-10 | GET | `/home` | No session | `302` → `/login` |

---

## Section C – Postman Collection Setup Summary

### Environment Variables
```
Variable     | Initial Value
-------------|-------------------
baseUrl      | http://localhost:8080
csrfToken    | (auto-populated)
```

### Suggested Request Order
1. Register ADMIN user (`POST /register`)
2. Register regular USER (`POST /register`)
3. Login flow: `GET /login` (extract CSRF) → `POST /login` as ADMIN
4. Add questions: `POST /addQuiz` (×2 or more)
5. View questions: `GET /QuizList`
6. Edit question: `GET /editQuiz/{id}` → `POST /editQuestion`
7. Delete question: `GET /deleteQuestion/{id}`
8. Logout: `POST /logout`
9. Login as USER: `GET /login` (extract CSRF) → `POST /login`
10. Take quiz: `GET /home` → `POST /submitQuiz`
11. View result: check response HTML
12. Logout: `POST /logout`

### CSRF Token Extraction Script
Add this to the **Tests** tab of every `GET` request for a form page:
```javascript
const html = pm.response.text();
const match = html.match(/name="_csrf"\s+value="([^"]+)"/);
if (match) {
    pm.collectionVariables.set("csrfToken", match[1]);
    console.log("CSRF token captured:", match[1]);
}
```

---

## Quick Smoke Test Checklist (Start-to-Finish)

- [ ] App starts on port 8080
- [ ] `/login` page loads
- [ ] `/register` page loads
- [ ] Register as ADMIN (`admin1` / `admin123` / `admin1@test.com`)
- [ ] Register as USER (`user1` / `user123` / `user1@test.com`)
- [ ] Login as ADMIN; `/home` automatically shows the QuizList (admin view)
- [ ] Add 3 questions via `/addQuiz`
- [ ] Edit one question via QuizList → Edit button
- [ ] Delete one question via QuizList → Delete button (confirm modal)
- [ ] Logout
- [ ] Login as USER; `/home` shows Quiz with remaining 2 questions
- [ ] Select answers and submit
- [ ] Result page shows score and percentage
- [ ] Retake Quiz button works
- [ ] Logout redirects to `/login?logout`