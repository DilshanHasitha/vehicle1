import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../merchant.test-samples';

import { MerchantFormService } from './merchant-form.service';

describe('Merchant Form Service', () => {
  let service: MerchantFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MerchantFormService);
  });

  describe('Service methods', () => {
    describe('createMerchantFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMerchantFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            merchantSecret: expect.any(Object),
            name: expect.any(Object),
            creditLimit: expect.any(Object),
            isActive: expect.any(Object),
            phone: expect.any(Object),
            addressLine1: expect.any(Object),
            addressLine2: expect.any(Object),
            city: expect.any(Object),
            country: expect.any(Object),
            percentage: expect.any(Object),
            creditScore: expect.any(Object),
            email: expect.any(Object),
            rating: expect.any(Object),
            leadTime: expect.any(Object),
            isSandBox: expect.any(Object),
            storeDescription: expect.any(Object),
            storeSecondaryDescription: expect.any(Object),
            images: expect.any(Object),
            exUsers: expect.any(Object),
          })
        );
      });

      it('passing IMerchant should create a new form with FormGroup', () => {
        const formGroup = service.createMerchantFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            merchantSecret: expect.any(Object),
            name: expect.any(Object),
            creditLimit: expect.any(Object),
            isActive: expect.any(Object),
            phone: expect.any(Object),
            addressLine1: expect.any(Object),
            addressLine2: expect.any(Object),
            city: expect.any(Object),
            country: expect.any(Object),
            percentage: expect.any(Object),
            creditScore: expect.any(Object),
            email: expect.any(Object),
            rating: expect.any(Object),
            leadTime: expect.any(Object),
            isSandBox: expect.any(Object),
            storeDescription: expect.any(Object),
            storeSecondaryDescription: expect.any(Object),
            images: expect.any(Object),
            exUsers: expect.any(Object),
          })
        );
      });
    });

    describe('getMerchant', () => {
      it('should return NewMerchant for default Merchant initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createMerchantFormGroup(sampleWithNewData);

        const merchant = service.getMerchant(formGroup) as any;

        expect(merchant).toMatchObject(sampleWithNewData);
      });

      it('should return NewMerchant for empty Merchant initial value', () => {
        const formGroup = service.createMerchantFormGroup();

        const merchant = service.getMerchant(formGroup) as any;

        expect(merchant).toMatchObject({});
      });

      it('should return IMerchant', () => {
        const formGroup = service.createMerchantFormGroup(sampleWithRequiredData);

        const merchant = service.getMerchant(formGroup) as any;

        expect(merchant).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMerchant should not enable id FormControl', () => {
        const formGroup = service.createMerchantFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMerchant should disable id FormControl', () => {
        const formGroup = service.createMerchantFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
