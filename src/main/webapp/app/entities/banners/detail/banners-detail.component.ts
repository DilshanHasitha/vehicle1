import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBanners } from '../banners.model';

@Component({
  selector: 'jhi-banners-detail',
  templateUrl: './banners-detail.component.html',
})
export class BannersDetailComponent implements OnInit {
  banners: IBanners | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ banners }) => {
      this.banners = banners;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
