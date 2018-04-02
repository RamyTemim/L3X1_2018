import {JsonObject, JsonProperty} from 'json2typescript';


export class Person {
  name: string;
  videos: string[];

  constructor(name: string, videos: string[]) {
    this.name = name;
    this.videos = videos;
  }
}
