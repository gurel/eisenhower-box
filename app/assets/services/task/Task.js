export default class Task {
  constructor(message = '', urgency = 1, importance = 1, status='inprogress', id='') {
    this.id = id;
    this.message = message;
    this.urgency = urgency;
    this.importance = importance;
    this.status = status;
  }
  fromObject(object) {
    this.id = object.id;
    this.message = object.message;
    this.urgency = object.urgency;
    this.importance = object.importance;
    this.status = object.status || 'inprogress';
    return this;
  }
  toObject() {
    const obj = {};
    if (this.id) { obj.id = this.id; }
    obj.message = this.message;
    obj.urgency = this.urgency;
    obj.importance = this.importance;
    obj.status = this.status;
    return obj;
  }
  static clone(task) {
    if (task === undefined) return undefined;
    return new Task(task.message, task.urgency, task.importance, task.status, task.id);
  }
}
