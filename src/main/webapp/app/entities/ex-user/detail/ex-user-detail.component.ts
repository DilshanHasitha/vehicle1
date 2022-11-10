import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IExUser } from '../ex-user.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-ex-user-detail',
  templateUrl: './ex-user-detail.component.html',
})
export class ExUserDetailComponent implements OnInit {
  exUser: IExUser | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ exUser }) => {
      this.exUser = exUser;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
