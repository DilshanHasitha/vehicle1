import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MerchantFormService } from './merchant-form.service';
import { MerchantService } from '../service/merchant.service';
import { IMerchant } from '../merchant.model';
import { IImages } from 'app/entities/images/images.model';
import { ImagesService } from 'app/entities/images/service/images.service';
import { IExUser } from 'app/entities/ex-user/ex-user.model';
import { ExUserService } from 'app/entities/ex-user/service/ex-user.service';

import { MerchantUpdateComponent } from './merchant-update.component';

describe('Merchant Management Update Component', () => {
  let comp: MerchantUpdateComponent;
  let fixture: ComponentFixture<MerchantUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let merchantFormService: MerchantFormService;
  let merchantService: MerchantService;
  let imagesService: ImagesService;
  let exUserService: ExUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MerchantUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(MerchantUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MerchantUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    merchantFormService = TestBed.inject(MerchantFormService);
    merchantService = TestBed.inject(MerchantService);
    imagesService = TestBed.inject(ImagesService);
    exUserService = TestBed.inject(ExUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Images query and add missing value', () => {
      const merchant: IMerchant = { id: 456 };
      const images: IImages = { id: 14368 };
      merchant.images = images;

      const imagesCollection: IImages[] = [{ id: 96591 }];
      jest.spyOn(imagesService, 'query').mockReturnValue(of(new HttpResponse({ body: imagesCollection })));
      const additionalImages = [images];
      const expectedCollection: IImages[] = [...additionalImages, ...imagesCollection];
      jest.spyOn(imagesService, 'addImagesToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ merchant });
      comp.ngOnInit();

      expect(imagesService.query).toHaveBeenCalled();
      expect(imagesService.addImagesToCollectionIfMissing).toHaveBeenCalledWith(
        imagesCollection,
        ...additionalImages.map(expect.objectContaining)
      );
      expect(comp.imagesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ExUser query and add missing value', () => {
      const merchant: IMerchant = { id: 456 };
      const exUsers: IExUser[] = [{ id: 73231 }];
      merchant.exUsers = exUsers;

      const exUserCollection: IExUser[] = [{ id: 53402 }];
      jest.spyOn(exUserService, 'query').mockReturnValue(of(new HttpResponse({ body: exUserCollection })));
      const additionalExUsers = [...exUsers];
      const expectedCollection: IExUser[] = [...additionalExUsers, ...exUserCollection];
      jest.spyOn(exUserService, 'addExUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ merchant });
      comp.ngOnInit();

      expect(exUserService.query).toHaveBeenCalled();
      expect(exUserService.addExUserToCollectionIfMissing).toHaveBeenCalledWith(
        exUserCollection,
        ...additionalExUsers.map(expect.objectContaining)
      );
      expect(comp.exUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const merchant: IMerchant = { id: 456 };
      const images: IImages = { id: 53914 };
      merchant.images = images;
      const exUser: IExUser = { id: 39791 };
      merchant.exUsers = [exUser];

      activatedRoute.data = of({ merchant });
      comp.ngOnInit();

      expect(comp.imagesSharedCollection).toContain(images);
      expect(comp.exUsersSharedCollection).toContain(exUser);
      expect(comp.merchant).toEqual(merchant);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMerchant>>();
      const merchant = { id: 123 };
      jest.spyOn(merchantFormService, 'getMerchant').mockReturnValue(merchant);
      jest.spyOn(merchantService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ merchant });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: merchant }));
      saveSubject.complete();

      // THEN
      expect(merchantFormService.getMerchant).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(merchantService.update).toHaveBeenCalledWith(expect.objectContaining(merchant));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMerchant>>();
      const merchant = { id: 123 };
      jest.spyOn(merchantFormService, 'getMerchant').mockReturnValue({ id: null });
      jest.spyOn(merchantService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ merchant: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: merchant }));
      saveSubject.complete();

      // THEN
      expect(merchantFormService.getMerchant).toHaveBeenCalled();
      expect(merchantService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMerchant>>();
      const merchant = { id: 123 };
      jest.spyOn(merchantService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ merchant });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(merchantService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareImages', () => {
      it('Should forward to imagesService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(imagesService, 'compareImages');
        comp.compareImages(entity, entity2);
        expect(imagesService.compareImages).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareExUser', () => {
      it('Should forward to exUserService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(exUserService, 'compareExUser');
        comp.compareExUser(entity, entity2);
        expect(exUserService.compareExUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
