import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { MerchantFormService, MerchantFormGroup } from './merchant-form.service';
import { IMerchant } from '../merchant.model';
import { MerchantService } from '../service/merchant.service';
import { IImages } from 'app/entities/images/images.model';
import { ImagesService } from 'app/entities/images/service/images.service';
import { IExUser } from 'app/entities/ex-user/ex-user.model';
import { ExUserService } from 'app/entities/ex-user/service/ex-user.service';

@Component({
  selector: 'jhi-merchant-update',
  templateUrl: './merchant-update.component.html',
})
export class MerchantUpdateComponent implements OnInit {
  isSaving = false;
  merchant: IMerchant | null = null;

  imagesSharedCollection: IImages[] = [];
  exUsersSharedCollection: IExUser[] = [];

  editForm: MerchantFormGroup = this.merchantFormService.createMerchantFormGroup();

  constructor(
    protected merchantService: MerchantService,
    protected merchantFormService: MerchantFormService,
    protected imagesService: ImagesService,
    protected exUserService: ExUserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareImages = (o1: IImages | null, o2: IImages | null): boolean => this.imagesService.compareImages(o1, o2);

  compareExUser = (o1: IExUser | null, o2: IExUser | null): boolean => this.exUserService.compareExUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ merchant }) => {
      this.merchant = merchant;
      if (merchant) {
        this.updateForm(merchant);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const merchant = this.merchantFormService.getMerchant(this.editForm);
    if (merchant.id !== null) {
      this.subscribeToSaveResponse(this.merchantService.update(merchant));
    } else {
      this.subscribeToSaveResponse(this.merchantService.create(merchant));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMerchant>>): void {
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

  protected updateForm(merchant: IMerchant): void {
    this.merchant = merchant;
    this.merchantFormService.resetForm(this.editForm, merchant);

    this.imagesSharedCollection = this.imagesService.addImagesToCollectionIfMissing<IImages>(this.imagesSharedCollection, merchant.images);
    this.exUsersSharedCollection = this.exUserService.addExUserToCollectionIfMissing<IExUser>(
      this.exUsersSharedCollection,
      ...(merchant.exUsers ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.imagesService
      .query()
      .pipe(map((res: HttpResponse<IImages[]>) => res.body ?? []))
      .pipe(map((images: IImages[]) => this.imagesService.addImagesToCollectionIfMissing<IImages>(images, this.merchant?.images)))
      .subscribe((images: IImages[]) => (this.imagesSharedCollection = images));

    this.exUserService
      .query()
      .pipe(map((res: HttpResponse<IExUser[]>) => res.body ?? []))
      .pipe(
        map((exUsers: IExUser[]) => this.exUserService.addExUserToCollectionIfMissing<IExUser>(exUsers, ...(this.merchant?.exUsers ?? [])))
      )
      .subscribe((exUsers: IExUser[]) => (this.exUsersSharedCollection = exUsers));
  }
}
