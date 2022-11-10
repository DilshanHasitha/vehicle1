import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { BannersFormService, BannersFormGroup } from './banners-form.service';
import { IBanners } from '../banners.model';
import { BannersService } from '../service/banners.service';
import { IImages } from 'app/entities/images/images.model';
import { ImagesService } from 'app/entities/images/service/images.service';

@Component({
  selector: 'jhi-banners-update',
  templateUrl: './banners-update.component.html',
})
export class BannersUpdateComponent implements OnInit {
  isSaving = false;
  banners: IBanners | null = null;

  imagesSharedCollection: IImages[] = [];

  editForm: BannersFormGroup = this.bannersFormService.createBannersFormGroup();

  constructor(
    protected bannersService: BannersService,
    protected bannersFormService: BannersFormService,
    protected imagesService: ImagesService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareImages = (o1: IImages | null, o2: IImages | null): boolean => this.imagesService.compareImages(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ banners }) => {
      this.banners = banners;
      if (banners) {
        this.updateForm(banners);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const banners = this.bannersFormService.getBanners(this.editForm);
    if (banners.id !== null) {
      this.subscribeToSaveResponse(this.bannersService.update(banners));
    } else {
      this.subscribeToSaveResponse(this.bannersService.create(banners));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBanners>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(banners: IBanners): void {
    this.banners = banners;
    this.bannersFormService.resetForm(this.editForm, banners);

    this.imagesSharedCollection = this.imagesService.addImagesToCollectionIfMissing<IImages>(
      this.imagesSharedCollection,
      ...(banners.images ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.imagesService
      .query()
      .pipe(map((res: HttpResponse<IImages[]>) => res.body ?? []))
      .pipe(map((images: IImages[]) => this.imagesService.addImagesToCollectionIfMissing<IImages>(images, ...(this.banners?.images ?? []))))
      .subscribe((images: IImages[]) => (this.imagesSharedCollection = images));
  }
}
