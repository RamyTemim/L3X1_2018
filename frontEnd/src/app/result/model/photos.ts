import {Person} from './person';

export class Photos {
  photos: Person[];

  constructor(persons: Person[]) {
    this.photos = persons;
  }
}
