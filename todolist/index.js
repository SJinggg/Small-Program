let todos = [];

function addTodo(text) {
  const todo = {
    text,
    checked: false,
    id: Date.now(),
    deleted: false,
  };

  todos.push(todo);
  renderTodo(todo);
}

const form = document.querySelector('.todo-form');
form.addEventListener('submit', event => {
  event.preventDefault();
  const input = document.querySelector('.todo-input');

  const text = input.value.trim();
  if (text !== '') {
    addTodo(text);
    input.value = '';
    input.focus();
  }
});

function renderTodo(todo){
  localStorage.setItem('todoItemsRef', JSON.stringify(todos));

  const todolist = document.querySelector('.todo-list');
  const item = document.querySelector(`[data-key='${todo.id}']`);
  
  if (todo.deleted) {
    item.remove();

    if(todolist.length === 0)
      todolist.innerHTML = '';
    return
  }

  const checked = todo.checked ? 'done' : '';
  const todoitem = document.createElement("li");
  
  todoitem.setAttribute('class', `todo-item ${checked}`);
  todoitem.setAttribute('data-key', todo.id);
  todoitem.innerHTML = `
    <input id="${todo.id}" type="checkbox"/>
    <label for="${todo.id}" class="tick"></label>
    <span>${todo.text}</span>
    <button class="delete-todo">
      <svg><use href="#delete-icon"></use></svg>
    </button> 
  `;

  if(item){
    todolist.replaceChild(todoitem, item)
  }
  else
    todolist.append(todoitem);
}

const todolist = document.querySelector('.todo-list');

todolist.addEventListener('click', event => {
  if (event.target.classList.contains('tick')) {
    const itemKey = event.target.parentElement.dataset.key;
    toggleDone(itemKey);
  }

  if (event.target.classList.contains('delete-todo')) {
    const itemKey = event.target.parentElement.dataset.key;
    deleteTodo(itemKey);
  }
});

function toggleDone(itemkey){
  const index = todos.findIndex(item => item.id == itemkey);
  todos[index].checked = !todos[index].checked;
  renderTodo(todos[index]);
}

function deleteTodo(itemkey){
  const index = todos.findIndex(item => item.id == itemkey);
  todos[index].deleted = true;
  renderTodo(todos[index]);
  todos = todos.filter(item => item.id != itemkey);
}


document.addEventListener('DOMContentLoaded', () => {
  const ref = localStorage.getItem('todoItemsRef');
  if (ref) {
    todos = JSON.parse(ref);
    todos.forEach(t => {
      renderTodo(t);
    });
  }
});