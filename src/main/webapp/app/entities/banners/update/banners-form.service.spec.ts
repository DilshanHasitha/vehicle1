import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../banners.test-samples';

import { BannersFormService } from './banners-form.service';

describe('Banners Form Service', () => {
  let service: BannersFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BannersFormService);
  });

  describe('Service methods', () => {
    describe('createBannersFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBannersFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            heading: expect.any(Object),
            description: expect.any(Object),
            link: expect.any(Object),
            images: expect.any(Object),
          })
        );
      });

      it('passing IBanners should create a new form with FormGroup', () => {
        const formGroup = service.createBannersFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            heading: expect.any(Object),
            description: expect.any(Object),
            link: expect.any(Object),
            images: expect.any(Object),
          })
        );
      });
    });

    describe('getBanners', () => {
      it('should return NewBanners for default Banners initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createBannersFormGroup(sampleWithNewData);

        const banners = service.getBanners(formGroup) as any;

        expect(banners).toMatchObject(sampleWithNewData);
      });

      it('should return NewBanners for empty Banners initial value', () => {
        const formGroup = service.createBannersFormGroup();

        const banners = service.getBanners(formGroup) as any;

        expect(banners).toMatchObject({});
      });

      it('should return IBanners', () => {
        const formGroup = service.createBannersFormGroup(sampleWithRequiredData);

        const banners = service.getBanners(formGroup) as any;

        expect(banners).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBanners should not enable id FormControl', () => {
        const formGroup = service.createBannersFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBanners should disable id FormControl', () => {
        const formGroup = service.createBannersFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
