import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { VehicleFormService, VehicleFormGroup } from './vehicle-form.service';
import { IVehicle } from '../vehicle.model';
import { VehicleService } from '../service/vehicle.service';
import { IMerchant } from 'app/entities/merchant/merchant.model';
import { MerchantService } from 'app/entities/merchant/service/merchant.service';

@Component({
  selector: 'jhi-vehicle-update',
  templateUrl: './vehicle-update.component.html',
})
export class VehicleUpdateComponent implements OnInit {
  isSaving = false;
  vehicle: IVehicle | null = null;

  merchantsSharedCollection: IMerchant[] = [];

  editForm: VehicleFormGroup = this.vehicleFormService.createVehicleFormGroup();

  constructor(
    protected vehicleService: VehicleService,
    protected vehicleFormService: VehicleFormService,
    protected merchantService: MerchantService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareMerchant = (o1: IMerchant | null, o2: IMerchant | null): boolean => this.merchantService.compareMerchant(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vehicle }) => {
      this.vehicle = vehicle;
      if (vehicle) {
        this.updateForm(vehicle);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vehicle = this.vehicleFormService.getVehicle(this.editForm);
    if (vehicle.id !== null) {
      this.subscribeToSaveResponse(this.vehicleService.update(vehicle));
    } else {
      this.subscribeToSaveResponse(this.vehicleService.create(vehicle));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVehicle>>): void {
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

  protected updateForm(vehicle: IVehicle): void {
    this.vehicle = vehicle;
    this.vehicleFormService.resetForm(this.editForm, vehicle);

    this.merchantsSharedCollection = this.merchantService.addMerchantToCollectionIfMissing<IMerchant>(
      this.merchantsSharedCollection,
      vehicle.merchant
    );
  }

  protected loadRelationshipsOptions(): void {
    this.merchantService
      .query()
      .pipe(map((res: HttpResponse<IMerchant[]>) => res.body ?? []))
      .pipe(
        map((merchants: IMerchant[]) => this.merchantService.addMerchantToCollectionIfMissing<IMerchant>(merchants, this.vehicle?.merchant))
      )
      .subscribe((merchants: IMerchant[]) => (this.merchantsSharedCollection = merchants));
  }
}
