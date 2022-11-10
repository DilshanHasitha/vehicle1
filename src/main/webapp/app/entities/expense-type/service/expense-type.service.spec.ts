import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IExpenseType } from '../expense-type.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../expense-type.test-samples';

import { ExpenseTypeService } from './expense-type.service';

const requireRestSample: IExpenseType = {
  ...sampleWithRequiredData,
};

describe('ExpenseType Service', () => {
  let service: ExpenseTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IExpenseType | IExpenseType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ExpenseTypeService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a ExpenseType', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const expenseType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(expenseType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ExpenseType', () => {
      const expenseType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(expenseType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ExpenseType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ExpenseType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ExpenseType', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addExpenseTypeToCollectionIfMissing', () => {
      it('should add a ExpenseType to an empty array', () => {
        const expenseType: IExpenseType = sampleWithRequiredData;
        expectedResult = service.addExpenseTypeToCollectionIfMissing([], expenseType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(expenseType);
      });

      it('should not add a ExpenseType to an array that contains it', () => {
        const expenseType: IExpenseType = sampleWithRequiredData;
        const expenseTypeCollection: IExpenseType[] = [
          {
            ...expenseType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addExpenseTypeToCollectionIfMissing(expenseTypeCollection, expenseType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ExpenseType to an array that doesn't contain it", () => {
        const expenseType: IExpenseType = sampleWithRequiredData;
        const expenseTypeCollection: IExpenseType[] = [sampleWithPartialData];
        expectedResult = service.addExpenseTypeToCollectionIfMissing(expenseTypeCollection, expenseType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(expenseType);
      });

      it('should add only unique ExpenseType to an array', () => {
        const expenseTypeArray: IExpenseType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const expenseTypeCollection: IExpenseType[] = [sampleWithRequiredData];
        expectedResult = service.addExpenseTypeToCollectionIfMissing(expenseTypeCollection, ...expenseTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const expenseType: IExpenseType = sampleWithRequiredData;
        const expenseType2: IExpenseType = sampleWithPartialData;
        expectedResult = service.addExpenseTypeToCollectionIfMissing([], expenseType, expenseType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(expenseType);
        expect(expectedResult).toContain(expenseType2);
      });

      it('should accept null and undefined values', () => {
        const expenseType: IExpenseType = sampleWithRequiredData;
        expectedResult = service.addExpenseTypeToCollectionIfMissing([], null, expenseType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(expenseType);
      });

      it('should return initial array if no ExpenseType is added', () => {
        const expenseTypeCollection: IExpenseType[] = [sampleWithRequiredData];
        expectedResult = service.addExpenseTypeToCollectionIfMissing(expenseTypeCollection, undefined, null);
        expect(expectedResult).toEqual(expenseTypeCollection);
      });
    });

    describe('compareExpenseType', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareExpenseType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareExpenseType(entity1, entity2);
        const compareResult2 = service.compareExpenseType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareExpenseType(entity1, entity2);
        const compareResult2 = service.compareExpenseType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareExpenseType(entity1, entity2);
        const compareResult2 = service.compareExpenseType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
